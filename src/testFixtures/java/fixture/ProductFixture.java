package fixture;

import com.hwamok.category.domain.Category;
import com.hwamok.product.domain.Product;

public class ProductFixture {
    public static Product createProduct(){
        return new Product("사과", "S001", 10000, null);
    }

    public static Product createProduct(Category category){
        return new Product("사과", "S001", 10000, category);
    }
}
