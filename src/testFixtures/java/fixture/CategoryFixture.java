package fixture;

import com.hwamok.category.domain.Category;

public class CategoryFixture {
    public static Category createCategory(){
        return new Category("화목한쇼핑몰", "CA001", "식품", 0, null);
    }

    public static Category createCategory(Category category){
        return new Category("화목한쇼핑몰", "CA002", "의류", 0, category);
    }

    public static Category createCategory(String branch){
        return new Category(branch, "CA003", "식품", 0, null);
    }
}
