package kz.kegoc.bln.helper.data;

import kz.kegoc.bln.entity.media.Batch;
import org.json.JSONException;
import org.json.JSONObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BatchHelper {
    public final static Long BATCH_ID = 1L;

    public static Batch newBatch() {
        Batch entity = new Batch();
        entity.setId(BATCH_ID);
        return entity;
    }

    public static Batch newBatch(Long id) {
        Batch entity = newBatch();
        entity.setId(id);
        return entity;
    }


    public static void assertBatch(Batch entity) {
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertTrue(entity.getId()>0);
    }

    public static void assertBatch(Batch entity1, Batch entity2) {
        assertNotNull(entity1);
        assertNotNull(entity1.getId());
        assertTrue(entity1.getId()>0);

        assertNotNull(entity2);
        assertNotNull(entity2.getId());
        assertTrue(entity2.getId()>0);

        assertEquals(entity1.getId(), entity2.getId());
    }


    public static String batchToJson(Batch entity) {
        JSONObject body = new JSONObject();

        try {
            if (entity.getId()!=null)
                body.put("id", entity.getId());
        }
        catch (JSONException e) {}

        return body.toString();
    }

}
