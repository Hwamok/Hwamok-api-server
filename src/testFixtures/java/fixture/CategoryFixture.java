package fixture;

import com.hwamok.category.domain.Category;

public class CategoryFixture {
    public static Category createCategory(){
        return new Category("식품", "CA001", "돼지고기", 0L, null);
    }

    public static Category createCategory(Category category){
        return new Category("식품", "CA002", "소고기", 0L, category);
    }

    public static Category createCategory(String branch){
        return new Category(branch, "CA003", "감자", 0L, null);
    }
}
