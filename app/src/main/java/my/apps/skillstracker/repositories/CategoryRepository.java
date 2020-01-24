package my.apps.skillstracker.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;
import my.apps.skillstracker.Category;
import my.apps.skillstracker.CategoryDao;
import my.apps.skillstracker.SkillDao;

public class CategoryRepository {

    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> mAllCategories;
    private Category mCategory;

    CategoryRepository(Application application){
        SkillRoomDatabase db = SkillRoomDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories(){
        return mAllCategories;
    }

    public Category getCategory(int id){ return mCategory; }

    public void insert (Category category) {
        new insertAsyncTask(mCategoryDao).execute(category);
    }

    public void delete(Category category) {
        new deleteAsyncTask(mCategoryDao).execute(category);
    }

    public void update(MyTaskParams params) { new updateAsyncTask(mCategoryDao).execute(params); }

    // AsyncTask methods implementations
    // ---------------------------------
    private static class deleteAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        deleteAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<MyTaskParams, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        updateAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MyTaskParams... params) {
            mAsyncTaskDao.update(params[0].getId(),
                    params[0].getName(),
                    params[0].getDescription(),
                    params[0].getType());
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public static class MyTaskParams {

        int id;
        String name;
        String description;
        int type;

        public MyTaskParams(int id, String name, String description, int experience) {
            this.name = name;
            this.description = description;
            this.id = id;
            this.type = experience;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getType() { return type; }
    }


}