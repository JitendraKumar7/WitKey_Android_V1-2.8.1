package app.witkey.live.utils.animations.giftbullet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GiftQueue {
    Map<String, List<GiftSendModel>> queue;

    public GiftQueue() {
        queue = new LinkedHashMap<>();
    }


    public List<GiftSendModel> getList(String key) {
        return queue.get(key);
    }

    public synchronized GiftSendModel removeTop() {
        GiftSendModel model = null;
        if (queue.size() > 0) {
            List<GiftSendModel> giftSendModels = getTopList();

            if (giftSendModels == null) {
                return model;
            }
            if (giftSendModels.size() > 0) {
                model = giftSendModels.remove(0);
            }
            if (giftSendModels.size() == 0) {
                queue.remove(model.getNickname());
            }
        }
        return model;
    }


    private List<GiftSendModel> getTopList() {
        Set<String> strings = queue.keySet();
        for (String string : strings) {
            return queue.get(string);
        }
        return null;
    }

    public synchronized void add(GiftSendModel model) {
        List<GiftSendModel> mapList = getList(model.getNickname());
        if (mapList == null) {
            List<GiftSendModel> l = new ArrayList<>();
            l.add(model);
            queue.put(model.getNickname(), l);
        } else {
            boolean isRepeat = false;
            for (GiftSendModel giftModel : mapList) {
                if (giftModel.getGiftURL().equals(model.getGiftURL())) {
                    giftModel.addGifCount(model.getGiftCount());
                    isRepeat = true;
                    break;
                }/*if (giftModel.getGiftRes() == model.getGiftRes()) {
                    giftModel.addGifCount(model.getGiftCount());
                    isRepeat = true;
                    break;
                }*/
            }
            if (!isRepeat) {
                mapList.add(model);
            }
        }
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }

}
