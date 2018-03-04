package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.helper.DataSetLoader;
import kz.kegoc.bln.helper.DbHelper;
import kz.kegoc.bln.repository.data.impl.BatchRepositoryImpl;
import org.junit.*;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static kz.kegoc.bln.helper.data.BatchHelper.BATCH_ID;
import static kz.kegoc.bln.helper.data.BatchHelper.assertBatch;
import static kz.kegoc.bln.helper.data.BatchHelper.newBatch;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class BatchRepositoryTest {
    private static final DbHelper dbHelper = new DbHelper();
    private static final BatchRepository repository = new BatchRepositoryImpl(dbHelper.getEntityManager());;
	private static final List<DataSetLoader> dataSetList = Arrays.asList(new DataSetLoader("apps", "data.xml"));

	private static final Long nonExistedId = 3l;
	private static final String nonExistedCode = "123";
	private static final String nonExistedName = "Бла бла бла";


	@BeforeClass
	public static void setUpClass()  {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
		
	@AfterClass
	public static void tearDownClass()  {
        dbHelper.closeDatabase();
	}
	
	
	@Before
	public void setUp() throws Exception {
        dbHelper.beginTransaction();
        dbHelper.cleanAndInsert(dataSetList);
	}
	
	@After
	public void tearDown() {
        dbHelper.commitTransaction();
	}


	//Success cases

	@Test
	public void theListBatchsMayBeSelected() {
		List<Batch> list = repository.selectAll();
		
		assertThat(list, is(not(nullValue())));
		assertThat(list, hasSize(2));
		
		assertThat(list.get(0), is(not(nullValue())));
        assertThat(list.get(0).getId(), is(not(nullValue())));
		assertBatch(list.get(0));

		assertThat(list.get(1), is(not(nullValue())));
		assertThat(list.get(1).getId(), is(not(nullValue())));
	}
	

	@Test
	public void existingBatchMayBeSelectedById() {
		Batch entity = repository.selectById(BATCH_ID);
		assertThat(entity, is(not(nullValue())));
		assertBatch(entity);
	}


	@Test
	public void newBatchMayBeInserted() {
		Batch origEntity = newBatch(null);

		Batch entity = repository.insert(origEntity);
		assertThat(entity.getId(), is(not(nullValue())));

        entity = repository.selectById(entity.getId());
		assertThat(entity, is(not(nullValue())));
		assertBatch(entity);
	}


	@Test
	public void existingBatchMayBeUpdated() {
		Batch origEntity = repository.selectById(BATCH_ID);;
        //origEntity.setName(origEntity.getName() + " (нов)");

		Batch entity = repository.update(origEntity);
		assertThat(entity, is(not(nullValue())));

        entity = repository.selectById(entity.getId());
		assertBatch(origEntity, entity);
	}


	@Test
	public void existingBatchMayBeDeleted() {
		boolean result = repository.delete(BATCH_ID);
        assertThat(result, is(equalTo(true)));

        Batch entity = repository.selectById(BATCH_ID);
        assertThat(entity, is(nullValue()));
    }



    //Fail cases

    @Test
    public void methodSelectByIdShouldReturnNullIfIdIsNotExists() {
        Batch entity = repository.selectById(nonExistedId);
        assertThat(entity, is(nullValue()));
    }

    @Test
    public void methodDeleteShouldReturnFalseIfIdIsNotExists() {
        boolean result = repository.delete(nonExistedId);
        assertThat(result, is(equalTo(false)));
    }
}
