package Final;
import java.io.*;
import java.util.*;

public class SMMS {
    static final String PRODUCTSFILE = "products.txt";
    static final String CUSTOMERSFILE = "customers.txt";
    static final String SALESFILE = "sales.txt";
    static final String MANAGERFILE = "manager.txt";

    static String[] productId = new String[100];
    static String[] productName = new String[100];
    static String[] productCategory = new String[100];
    static double[] productPrice = new double[100];
    static int[] productStock = new int[100];
    static String[] productExpiry = new String[100];
    static int productCount = 0;

    static int[] customerId = new int[100];
    static String[] customerName = new String[100];
    static String[] customerPhone = new String[100];
    static int customerCount = 0;

    static int[] saleId = new int[100];
    static int[] saleCustomerId = new int[100];
    static String[] saleProductId = new String[100];
    static int[] saleQuantity = new int[100];
    static double[] saleAmount = new double[100];
    static int saleCount = 0;

    static String managerUsername = "admin";
    static String managerPassword = "admin123";

    static Scanner sc = new Scanner(System.in);

    static String[] cartProductId = new String[100];
    static String[] cartProductName = new String[100];
    static double[] cartProductPrice = new double[100];
    static int[] cartQuantity = new int[100];
    static int cartCount = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SUPERMARKET MANAGEMENT SYSTEM");
        System.out.println("========================================");

        loadAllData();
        loadManagerCredentials();
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n====== MAIN MENU ======");
            System.out.println("1. Manager Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        if (managerLogin()) {
                            managerMenu();
                        }
                        break;
                    case 2:
                        customerMenu();
                        break;
                    case 3:
                        saveAllData();
                        System.out.println("Thank you! Data saved successfully.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        }
    }

    static void managerMenu() {
        while (true) {
            System.out.println("\n====== MANAGER MENU ======");
            System.out.println("1. Product Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Sales Reports");
            System.out.println("4. Manager Settings");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: productManagementMenu(); break;
                    case 2: customerManagementMenu(); break;
                    case 3: salesReportsMenu(); break;
                    case 4: managerSettingsMenu(); break;
                    case 5: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void customerMenu() {
        System.out.print("\nEnter your Customer ID (or 0 to register): ");
        int custId = sc.nextInt();
        sc.nextLine();

        if (custId == 0) {
            registerNewCustomer();
            return;
        }

        int custIndex = findCustomerById(custId);
        if (custIndex == -1) {
            System.out.println("Customer not found! Please register first.");
            return;
        }

        System.out.println("\nWelcome, " + customerName[custIndex] + "!");
        System.out.println("Current Offers: Get 10% discount on purchases of Rs. 1000 or more!");

        cartCount = 0;

        while (true) {
            System.out.println("\n====== CUSTOMER MENU ======");
            System.out.println("1. View Available Products");
            System.out.println("2. Search Products");
            System.out.println("3. Add Product to Cart");
            System.out.println("4. View My Cart");
            System.out.println("5. Update Cart Item");
            System.out.println("6. Remove Item from Cart");
            System.out.println("7. Checkout and Generate Bill");
            System.out.println("8. View My Purchase History");
            System.out.println("9. View Available Offers");
            System.out.println("10. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: viewAllProducts(); break;
                    case 2: searchProductMenu(); break;
                    case 3: addToCart(custId); break;
                    case 4: viewCart(); break;
                    case 5: updateCartItem(); break;
                    case 6: removeFromCart(); break;
                    case 7: checkoutAndGenerateBill(custId); break;
                    case 8: viewCustomerPurchaseHistory(custId); break;
                    case 9: viewOffers(); break;
                    case 10: 
                        cartCount = 0;
                        return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void addToCart(int custId) {
        if (productCount == 0) {
            System.out.println("No products available!");
            return;
        }

        try {
            viewAllProducts();
            System.out.print("\nEnter Product ID to add to cart: ");
            String prodId = sc.nextLine();

            int prodIndex = findProductByIdIndex(prodId);
            if (prodIndex == -1) {
                System.out.println("Product not found!");
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();
            sc.nextLine();

            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }

            if (quantity > productStock[prodIndex]) {
                System.out.println("Insufficient stock! Available: " + productStock[prodIndex]);
                return;
            }

            int cartIndex = findProductInCart(prodId);
            if (cartIndex != -1) {
                int newQuantity = cartQuantity[cartIndex] + quantity;
                if (newQuantity > productStock[prodIndex]) {
                    System.out.println("Cannot add more than available stock!");
                    return;
                }
                cartQuantity[cartIndex] = newQuantity;
                System.out.println("Updated quantity in cart. Total quantity: " + newQuantity);
            } else {
                cartProductId[cartCount] = prodId;
                cartProductName[cartCount] = productName[prodIndex];
                cartProductPrice[cartCount] = productPrice[prodIndex];
                cartQuantity[cartCount] = quantity;
                cartCount++;
                System.out.println("Product added to cart successfully!");
            }

            System.out.println("\nCurrent Cart Status:");
            viewCart();

        } catch (Exception e) {
            System.out.println("Error adding to cart: " + e.getMessage());
            sc.nextLine();
        }
    }

    static void viewCart() {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        System.out.println("\n========== SHOPPING CART ==========");
        System.out.printf("%-8s %-20s %-10s %-8s %-12s\n",
                "Item No", "Product Name", "Price", "Qty", "Subtotal");
        System.out.println("------------------------------------------------------------");

        double total = 0;
        for (int i = 0; i < cartCount; i++) {
            double subtotal = cartProductPrice[i] * cartQuantity[i];
            total += subtotal;
            System.out.printf("%-8d %-20s Rs%-9.2f %-8d Rs%-11.2f\n",
                    i + 1, cartProductName[i], cartProductPrice[i], 
                    cartQuantity[i], subtotal);
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("%-47s Rs%-11.2f\n", "TOTAL:", total);
        
        if (total >= 1000) {
            double discount = total * 0.10;
            double discountedTotal = total - discount;
            System.out.printf("%-47s Rs%-11.2f\n", "10% DISCOUNT:", discount);
            System.out.printf("%-47s Rs%-11.2f\n", "FINAL AMOUNT:", discountedTotal);
        }
    }

    static void updateCartItem() {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        viewCart();
        System.out.print("\nEnter Item No to update (or 0 to cancel): ");
        int itemNo = sc.nextInt();
        sc.nextLine();

        if (itemNo == 0) return;
        if (itemNo < 1 || itemNo > cartCount) {
            System.out.println("Invalid item number!");
            return;
        }

        int cartIndex = itemNo - 1;
        String prodId = cartProductId[cartIndex];
        int prodIndex = findProductByIdIndex(prodId);

        System.out.println("\nCurrent quantity: " + cartQuantity[cartIndex]);
        System.out.print("Enter new quantity: ");
        int newQuantity = sc.nextInt();
        sc.nextLine();

        if (newQuantity <= 0) {
            System.out.println("Invalid quantity! Item will be removed.");
            removeFromCartAtIndex(cartIndex);
            return;
        }

        if (newQuantity > productStock[prodIndex]) {
            System.out.println("Insufficient stock! Available: " + productStock[prodIndex]);
            return;
        }

        cartQuantity[cartIndex] = newQuantity;
        System.out.println("Cart updated successfully!");
        viewCart();
    }

    static void removeFromCart() {
        if (cartCount == 0) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        viewCart();
        System.out.print("\nEnter Item No to remove (or 0 to cancel): ");
        int itemNo = sc.nextInt();
        sc.nextLine();

        if (itemNo == 0) return;
        if (itemNo < 1 || itemNo > cartCount) {
            System.out.println("Invalid item number!");
            return;
        }

        removeFromCartAtIndex(itemNo - 1);
        System.out.println("Item removed from cart!");
        viewCart();
    }

    static void removeFromCartAtIndex(int index) {
        for (int i = index; i < cartCount - 1; i++) {
            cartProductId[i] = cartProductId[i + 1];
            cartProductName[i] = cartProductName[i + 1];
            cartProductPrice[i] = cartProductPrice[i + 1];
            cartQuantity[i] = cartQuantity[i + 1];
        }
        cartCount--;
    }

    static void checkoutAndGenerateBill(int custId) {
        if (cartCount == 0) {
            System.out.println("\nCannot checkout - cart is empty!");
            return;
        }

        for (int i = 0; i < cartCount; i++) {
            String prodId = cartProductId[i];
            int prodIndex = findProductByIdIndex(prodId);
            if (cartQuantity[i] > productStock[prodIndex]) {
                System.out.println("Error: Insufficient stock for " + cartProductName[i]);
                System.out.println("Required: " + cartQuantity[i] + ", Available: " + productStock[prodIndex]);
                return;
            }
        }

        viewCart();
        System.out.print("\nConfirm checkout? (yes/no): ");
        String confirm = sc.nextLine().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("Checkout cancelled.");
            return;
        }

        double totalAmount = 0;
        double totalDiscount = 0;

        for (int i = 0; i < cartCount; i++) {
            String prodId = cartProductId[i];
            int prodIndex = findProductByIdIndex(prodId);
            
            double itemTotal = cartProductPrice[i] * cartQuantity[i];
            totalAmount += itemTotal;
            
            productStock[prodIndex] -= cartQuantity[i];
            
            saleId[saleCount] = generateSaleId();
            saleCustomerId[saleCount] = custId;
            saleProductId[saleCount] = prodId;
            saleQuantity[saleCount] = cartQuantity[i];
            saleAmount[saleCount] = itemTotal;
            saleCount++;
        }

        double finalAmount = totalAmount;
        if (totalAmount >= 1000) {
            totalDiscount = totalAmount * 0.10;
            finalAmount = totalAmount - totalDiscount;
        }

        saveProductsToFile();
        saveSalesToFile();

        System.out.println("\n========== BILL RECEIPT ==========");
        System.out.println("Receipt No: " + saleId[saleCount - 1]);
        System.out.println("Customer: " + customerName[findCustomerById(custId)]);
        System.out.println("-----------------------------------");
        
        for (int i = 0; i < cartCount; i++) {
            System.out.printf("%-20s %-5d x Rs%-7.2f = Rs%-8.2f\n",
                    cartProductName[i], cartQuantity[i], 
                    cartProductPrice[i], cartProductPrice[i] * cartQuantity[i]);
        }
        
        System.out.println("-----------------------------------");
        System.out.printf("%-30s Rs%-8.2f\n", "Subtotal:", totalAmount);
        
        if (totalDiscount > 0) {
            System.out.printf("%-30s Rs%-8.2f\n", "Discount (10%):", totalDiscount);
            System.out.printf("%-30s Rs%-8.2f\n", "Total Amount:", finalAmount);
        }
        
        System.out.println("===================================");
        System.out.println("Thank you for your purchase!");

        cartCount = 0;
    }

    static int findProductInCart(String prodId) {
        for (int i = 0; i < cartCount; i++) {
            if (cartProductId[i].equals(prodId)) {
                return i;
            }
        }
        return -1;
    }

    static void viewOffers() {
        System.out.println("\n====== AVAILABLE OFFERS ======");
        System.out.println("1. Get 10% discount on purchases of Rs. 1000 or more!");
        System.out.println("2. Regular customers get special monthly discounts");
        System.out.println("3. Bulk purchases (10+ items) get additional 5% off");
        System.out.println("=================================");
    }

    static void productManagementMenu() {
        while (true) {
            System.out.println("\n===== PRODUCT MANAGEMENT =====");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Products");
            System.out.println("6. Low Stock Alert");
            System.out.println("7. Inventory Reports");
            System.out.println("8. Back");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: viewAllProducts(); break;
                    case 3: updateProduct(); break;
                    case 4: deleteProduct(); break;
                    case 5: searchProductMenu(); break;
                    case 6: showLowStockProducts(); break;
                    case 7: inventoryReportsMenu(); break; 
                    case 8: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void addProduct() {
        if (productCount >= 100) {
            System.out.println("Product storage full!");
            return;
        }

        try {
            String id = generateProductId();
            System.out.println("\n--- Add New Product (ID: " + id + ") ---");

            System.out.print("Enter Product Name: ");
            productName[productCount] = sc.nextLine();

            System.out.print("Enter Category: ");
            productCategory[productCount] = sc.nextLine();

            System.out.print("Enter Price: ");
            productPrice[productCount] = sc.nextDouble();

            System.out.print("Enter Stock Quantity: ");
            productStock[productCount] = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Expiry Date (yyyy-mm-dd): ");  
            productExpiry[productCount] = sc.nextLine();

            productId[productCount] = id;
            productCount++;

            saveProductsToFile();
            System.out.println("Product added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
            sc.nextLine();
        }
    }

    static void viewAllProducts() {
        if (productCount == 0) {
            System.out.println("\nNo products available!");
            return;
        }

        System.out.println("\n========== PRODUCT LIST ==========");
        System.out.printf("%-8s %-15s %-12s %-10s %-8s\n",
                "ID", "Name", "Category", "Price", "Stock");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < productCount; i++) {
            System.out.printf("%-8s %-15s %-12s %-10.2f %-8d\n",
                    productId[i], productName[i], productCategory[i],
                    productPrice[i], productStock[i]);
        }
    }

    static void updateProduct() {
        System.out.print("\nEnter Product ID to update: ");
        String id = sc.nextLine();

        int index = findProductByIdIndex(id);
        if (index == -1) {
            System.out.println("Product not found!");
            return;
        }

        try {
            System.out.println("\n--- Update Product: " + productName[index] + " ---");

            System.out.print("Enter New Name (current: " + productName[index] + "): ");
            productName[index] = sc.nextLine();

            System.out.print("Enter New Category (current: " + productCategory[index] + "): ");
            productCategory[index] = sc.nextLine();

            System.out.print("Enter New Price (current: " + productPrice[index] + "): ");
            productPrice[index] = sc.nextDouble();

            System.out.print("Enter New Stock (current: " + productStock[index] + "): ");
            productStock[index] = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Expiry Date (current: " + productExpiry[index] + "): ");  
            productExpiry[index] = sc.nextLine();

            saveProductsToFile();
            System.out.println("Product updated successfully!");

        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            sc.nextLine();
        }
    }

    static void deleteProduct() {
        System.out.print("\nEnter Product ID to delete: ");
        String id = sc.nextLine();

        int index = findProductByIdIndex(id);
        if (index == -1) {
            System.out.println("Product not found!");
            return;
        }

        for (int i = index; i < productCount - 1; i++) {
            productId[i] = productId[i + 1];
            productName[i] = productName[i + 1];
            productCategory[i] = productCategory[i + 1];
            productPrice[i] = productPrice[i + 1];
            productStock[i] = productStock[i + 1];
            productExpiry[i] = productExpiry[i + 1];
        }

        productCount--;
        saveProductsToFile();
        System.out.println("Product deleted successfully!");
    }

    static void searchProductMenu() {
        System.out.println("\n--- Search Products ---");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Category");
        System.out.println("3. Search by Price Range");
        System.out.println("4. Recursive Search by Name");
        System.out.print("Enter choice: ");

        try {
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: searchByName(); break;
                case 2: searchByCategory(); break;
                case 3: searchByPriceRange(); break;
                case 4: recursiveProductSearch(); break;
                default: System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }

    static void searchByName() {
        System.out.print("Enter product name to search: ");
        String keyword = sc.nextLine().toLowerCase();

        boolean found = false;
        System.out.println("\n--- Search Results ---");

        for (int i = 0; i < productCount; i++) {
            if (productName[i].toLowerCase().contains(keyword)) {
                if (!found) {
                    System.out.printf("%-8s %-15s %-12s %-10s %-8s\n",
                            "ID", "Name", "Category", "Price", "Stock");
                    System.out.println("--------------------------------------------------");
                }
                System.out.printf("%-8s %-15s %-12s %-10.2f %-8d\n",
                        productId[i], productName[i], productCategory[i],
                        productPrice[i], productStock[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No products found!");
        }
    }

    static void searchByCategory() {
        System.out.print("Enter category to search: ");
        String category = sc.nextLine().toLowerCase();

        System.out.println("\n--- Search Results ---");
        System.out.printf("%-8s %-15s %-12s %-10s %-8s\n",
                "ID", "Name", "Category", "Price", "Stock");
        System.out.println("--------------------------------------------------");

        recursiveSearchByCategory(0, category);
    }

    //recursive search(only name)
    static void recursiveProductSearch() {
        System.out.print("Enter name keyword: ");
        String key = sc.nextLine().toLowerCase();
    
        boolean[] foundFlag = {false}; // flag to track if any product matches

        recursiveSearchHelper(0, key, foundFlag);

        if (!foundFlag[0]) {
            System.out.println("\nNo products found!");
        }

    pause();
    }

    static void recursiveSearchHelper(int index, String key, boolean[] foundFlag) {
       if (index >= productCount) return;

      if (productName[index].toLowerCase().contains(key)) {
        if (!foundFlag[0]) {
            System.out.println("\n--- Search Results ---");
            printProductTableHeader(); // print header only once
            foundFlag[0] = true;
        }
        printProductRow(index);
      }

       recursiveSearchHelper(index + 1, key, foundFlag);
    }


    static void recursiveSearchByCategory(int index, String category) {
    if (index >= productCount) return;

    if (productCategory[index].equalsIgnoreCase(category)) {
        printProductRow(index);
    }

    recursiveSearchByCategory(index + 1, category);
    }

        

    static void searchByPriceRange() {
        try {
            System.out.print("Enter minimum price: ");
            double minPrice = sc.nextDouble();

            System.out.print("Enter maximum price: ");
            double maxPrice = sc.nextDouble();
            sc.nextLine();

            boolean found = false;
            System.out.println("\n--- Products in Range ---");

            for (int i = 0; i < productCount; i++) {
                if (productPrice[i] >= minPrice && productPrice[i] <= maxPrice) {
                    if (!found) {
                        System.out.printf("%-8s %-15s %-12s %-10s %-8s\n",
                                "ID", "Name", "Category", "Price", "Stock");
                        System.out.println("--------------------------------------------------");
                    }
                    System.out.printf("%-8s %-15s %-12s %-10.2f %-8d\n",
                            productId[i], productName[i], productCategory[i],
                            productPrice[i], productStock[i]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No products found in this price range!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }
    // ===================== INVENTORY REPORTS MENU =====================
    static void inventoryReportsMenu() {
        System.out.println("\n===== INVENTORY REPORTS =====");
        System.out.println("1. Low Stock Alert");
        System.out.println("2. Out of Stock");
        System.out.println("3. Category Wise Count");
        System.out.println("4. Total Inventory Value");
        System.out.println("5. Back");
        System.out.print("Enter choice: ");
        
        try {
            int ch = sc.nextInt();
            sc.nextLine();
            switch (ch) {
                case 1: showLowStockProducts(); break;
                case 2: showOutOfStock(); break;
                case 3: showCategoryWiseInventory(); break;
                case 4: showTotalInventoryValue(); break;
                case 5: return;
                default: System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }

    static void showOutOfStock() {
        boolean found = false;
        System.out.println("\n--- OUT OF STOCK PRODUCTS ---");
        for (int i = 0; i < productCount; i++) {
            if (productStock[i] == 0) {
                if (!found) {
                    printProductTableHeader();
                }
                printProductRow(i);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No out of stock products!");
        }
        pause();
    }

    static void showCategoryWiseInventory() {
        System.out.println("\n--- CATEGORY WISE PRODUCT COUNT ---");
        boolean[] visited = new boolean[productCount];
        for (int i = 0; i < productCount; i++) {
            if (visited[i]) continue;
            int count = 1;
            for (int j = i + 1; j < productCount; j++) {
                if (productCategory[i].equalsIgnoreCase(productCategory[j])) {
                    count++;
                    visited[j] = true;
                }
            }
            System.out.println(productCategory[i] + " : " + count + " products");
        }
        pause();
    }

    static void showTotalInventoryValue() {
        double total = 0;
        for (int i = 0; i < productCount; i++) {
            total += productPrice[i] * productStock[i];
        }
        System.out.printf("\nTotal Inventory Value = Rs %.2f\n", total);
        pause();
    }

    static void printProductTableHeader() {
    System.out.printf("%-8s %-15s %-12s %-10s %-8s\n",
            "ID", "Name", "Category", "Price", "Stock");
    System.out.println("--------------------------------------------------");
}

static void printProductRow(int i) {
    System.out.printf("%-8s %-15s %-12s %-10.2f %-8d\n",
            productId[i], productName[i], productCategory[i],
            productPrice[i], productStock[i]);
}

static void pause() {
    System.out.println("\nPress Enter to continue...");
    sc.nextLine();
}

static void showLowStockProducts() {
    boolean found = false;
    System.out.println("\n--- LOW STOCK PRODUCTS (<=5) ---");
    for (int i = 0; i < productCount; i++) {
        if (productStock[i] <= 5) {
            if (!found) printProductTableHeader();
            printProductRow(i);
            found = true;
        }
    }
    if (!found) System.out.println("No low stock products!");
    pause();
}


    static void customerManagementMenu() {
        while (true) {
            System.out.println("\n===== CUSTOMER MANAGEMENT =====");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Search Customer");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: addCustomer(); break;
                    case 2: viewAllCustomers(); break;
                    case 3: updateCustomer(); break;
                    case 4: deleteCustomer(); break;
                    case 5: searchCustomer(); break;
                    case 6: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void registerNewCustomer() {
        addCustomer();
    }

    static void addCustomer() {
        if (customerCount >= 100) {
            System.out.println("Customer storage full!");
            return;
        }

        try {
            int id = generateCustomerId();
            System.out.println("\n--- Add New Customer (ID: " + id + ") ---");

            System.out.print("Enter Customer Name: ");
            customerName[customerCount] = sc.nextLine();

            System.out.print("Enter Phone Number: ");
            customerPhone[customerCount] = sc.nextLine();

            customerId[customerCount] = id;
            customerCount++;

            saveCustomersToFile();
            System.out.println("Customer registered successfully! Your ID is: " + id);
        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
            sc.nextLine();
        }
    }

    static void viewAllCustomers() {
        if (customerCount == 0) {
            System.out.println("\nNo customers registered!");
            return;
        }

        System.out.println("\n========== CUSTOMER LIST ==========");
        System.out.printf("%-8s %-20s %-15s\n", "ID", "Name", "Phone");
        System.out.println("------------------------------------------");

        for (int i = 0; i < customerCount; i++) {
            System.out.printf("%-8d %-20s %-15s\n",
                    customerId[i], customerName[i], customerPhone[i]);
        }
    }

    static void updateCustomer() {
        System.out.print("\nEnter Customer ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        int index = findCustomerById(id);
        if (index == -1) {
            System.out.println("Customer not found!");
            return;
        }

        try {
            System.out.println("\n--- Update Customer: " + customerName[index] + " ---");

            System.out.print("Enter New Name (current: " + customerName[index] + "): ");
            customerName[index] = sc.nextLine();

            System.out.print("Enter New Phone (current: " + customerPhone[index] + "): ");
            customerPhone[index] = sc.nextLine();

            saveCustomersToFile();
            System.out.println("Customer updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
            sc.nextLine();
        }
    }

    static void deleteCustomer() {
        System.out.print("\nEnter Customer ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        int index = findCustomerById(id);
        if (index == -1) {
            System.out.println("Customer not found!");
            return;
        }

        for (int i = index; i < customerCount - 1; i++) {
            customerId[i] = customerId[i + 1];
            customerName[i] = customerName[i + 1];
            customerPhone[i] = customerPhone[i + 1];
        }

        customerCount--;
        saveCustomersToFile();
        System.out.println("Customer deleted successfully!");
    }

    static void searchCustomer() {
        System.out.print("\nEnter Customer ID to search: ");
        int id = sc.nextInt();
        sc.nextLine();

        int index = findCustomerById(id);
        if (index == -1) {
            System.out.println("Customer not found!");
        } else {
            System.out.println("\n--- Customer Details ---");
            System.out.println("ID: " + customerId[index]);
            System.out.println("Name: " + customerName[index]);
            System.out.println("Phone: " + customerPhone[index]);
        }
    }

    static int findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customerId[i] == id) {
                return i;
            }
        }
        return -1;
    }

    static void viewCustomerPurchaseHistory(int custId) {
        boolean found = false;
        System.out.println("\n========== PURCHASE HISTORY ==========");

        for (int i = 0; i < saleCount; i++) {
            if (saleCustomerId[i] == custId) {
                if (!found) {
                    System.out.printf("%-8s %-12s %-10s %-10s\n",
                            "Sale ID", "Product ID", "Quantity", "Amount");
                    System.out.println("------------------------------------------");
                    found = true;
                }
                System.out.printf("%-8d %-12s %-10d Rs %-10.2f\n",
                        saleId[i], saleProductId[i], saleQuantity[i], saleAmount[i]);
            }
        }

        if (!found) {
            System.out.println("No purchase history found!");
        }
    }

    static void salesReportsMenu() {
        while (true) {
            System.out.println("\n===== SALES REPORTS =====");
            System.out.println("1. View All Sales");
            System.out.println("2. Total Revenue");
            System.out.println("3. Sales by Customer");
            System.out.println("4. Top Selling Products");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: viewAllSales(); break;
                    case 2: showTotalRevenue(); break;
                    case 3: salesByCustomer(); break;
                    case 4: showTopSellingProducts(); break;
                    case 5: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void managerSettingsMenu() {
        while (true) {
            System.out.println("\n===== MANAGER SETTINGS =====");
            System.out.println("1. View All Data");
            System.out.println("2. Change Password");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: viewAllData(); break;
                    case 2: changeManagerPassword(); break;
                    case 3: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    static void viewAllData() {
        System.out.println("\n========== SYSTEM DATA OVERVIEW ==========");
        
        System.out.println("\n--- PRODUCTS (" + productCount + " items) ---");
        viewAllProducts();

        System.out.println("\n--- CUSTOMERS (" + customerCount + " customers) ---");
        viewAllCustomers();

        System.out.println("\n--- SALES (" + saleCount + " transactions) ---");
        viewAllSales();

        System.out.println("\n--- SYSTEM SUMMARY ---");
        System.out.println("Total Products: " + productCount);
        System.out.println("Total Customers: " + customerCount);
        System.out.println("Total Sales: " + saleCount);
        
        double totalRevenue = 0;
        for (int i = 0; i < saleCount; i++) {
            totalRevenue += saleAmount[i];
        }
        System.out.printf("Total Revenue: Rs %.2f\n", totalRevenue);
        System.out.println("==========================================");
    }

    static boolean managerLogin() {
        System.out.println("\n===== MANAGER LOGIN =====");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (username.equals(managerUsername) && password.equals(managerPassword)) {
            System.out.println("Login successful! Welcome Manager.");
            return true;
        } else {
            System.out.println("Invalid credentials! Access denied.");
            return false;
        }
    }

    static int findProductByIdIndex(String id) {
        for (int i = 0; i < productCount; i++) {
            if (productId[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static void loadAllData() {
        loadProductsFromFile();
        loadCustomersFromFile();
        loadSalesFromFile();
        System.out.println("All data loaded successfully!");
    }

    static void saveAllData() {
        saveProductsToFile();
        saveCustomersToFile();
        saveSalesToFile();
    }

    static void loadManagerCredentials() {
        try {
            File f = new File(MANAGERFILE);
            if (!f.exists()) {
                return;
            }
            Scanner reader = new Scanner(f);
            if (reader.hasNextLine()) {
                managerUsername = reader.nextLine().trim();
            }
            if (reader.hasNextLine()) {
                managerPassword = reader.nextLine().trim();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading manager credentials: " + e.getMessage());
        }
    }

    static void saveManagerCredentials() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(MANAGERFILE));
            writer.println(managerUsername);
            writer.println(managerPassword);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving manager credentials: " + e.getMessage());
        }
    }

    static void viewAllSales() {
        if (saleCount == 0) {
            System.out.println("\nNo sales recorded!");
            return;
        }

        System.out.println("\n========== SALES LIST ==========");
        System.out.printf("%-8s %-12s %-12s %-10s %-10s\n",
                "Sale ID", "Customer ID", "Product ID", "Quantity", "Amount");
        System.out.println("--------------------------------------------------------");

        for (int i = 0; i < saleCount; i++) {
            System.out.printf("%-8d %-12d %-12s %-10d Rs %-10.2f\n",
                    saleId[i], saleCustomerId[i], saleProductId[i],
                    saleQuantity[i], saleAmount[i]);
        }
    }

    static void showTotalRevenue() {
        double totalRevenue = 0;
        for (int i = 0; i < saleCount; i++) {
            totalRevenue += saleAmount[i];
        }

        System.out.println("\n========== REVENUE REPORT ==========");
        System.out.println("Total Sales: " + saleCount);
        System.out.printf("Total Revenue: Rs %.2f\n", totalRevenue);
        System.out.println("====================================");
    }

    static void salesByCustomer() {
        System.out.print("\nEnter Customer ID: ");
        int custId = sc.nextInt();
        sc.nextLine();

        int custIndex = findCustomerById(custId);
        if (custIndex == -1) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("\n--- Sales for: " + customerName[custIndex] + " ---");
        double totalSpent = 0;
        int totalPurchases = 0;

        for (int i = 0; i < saleCount; i++) {
            if (saleCustomerId[i] == custId) {
                totalSpent += saleAmount[i];
                totalPurchases++;
            }
        }

        System.out.println("Total Purchases: " + totalPurchases);
        System.out.printf("Total Amount Spent: Rs %.2f\n", totalSpent);
    }

    static void showTopSellingProducts() {
        if (saleCount == 0) {
            System.out.println("No sales data available!");
            return;
        }

        String[] soldProducts = new String[productCount];
        int[] soldQuantities = new int[productCount];
        int uniqueCount = 0;

        for (int i = 0; i < saleCount; i++) {
            String prodId = saleProductId[i];
            boolean found = false;

            for (int j = 0; j < uniqueCount; j++) {
                if (soldProducts[j].equals(prodId)) {
                    soldQuantities[j] += saleQuantity[i];
                    found = true;
                    break;
                }
            }

            if (!found) {
                soldProducts[uniqueCount] = prodId;
                soldQuantities[uniqueCount] = saleQuantity[i];
                uniqueCount++;
            }
        }

        System.out.println("\n========== TOP SELLING PRODUCTS ==========");
        System.out.printf("%-12s %-20s %-15s\n", "Product ID", "Product Name", "Total Sold");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < uniqueCount; i++) {
            int prodIndex = findProductByIdIndex(soldProducts[i]);
            if (prodIndex != -1) {
                System.out.printf("%-12s %-20s %-15d\n",
                        soldProducts[i], productName[prodIndex], soldQuantities[i]);
            }
        }
    }

    static void saveProductsToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTSFILE));
            for (int i = 0; i < productCount; i++) {
                writer.println(productId[i] + "," +
                        productName[i] + "," +
                        productCategory[i] + "," +
                        productPrice[i] + "," +
                        productStock[i]+","+
                        productExpiry[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    static void loadProductsFromFile() {
        try {
            File f = new File(PRODUCTSFILE);
            if (!f.exists()) return;

            Scanner reader = new Scanner(f);
            productCount = 0;

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    productId[productCount] = parts[0];
                    productName[productCount] = parts[1];
                    productCategory[productCount] = parts[2];
                    productPrice[productCount] = Double.parseDouble(parts[3]);
                    productStock[productCount] = Integer.parseInt(parts[4]);
                    productExpiry[productCount] = (parts.length > 5) ? parts[5] : "N/A";
                    productCount++;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    static void saveCustomersToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERSFILE));
            for (int i = 0; i < customerCount; i++) {
                writer.println(customerId[i] + "," +
                        customerName[i] + "," +
                        customerPhone[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    static void loadCustomersFromFile() {
        try {
            File f = new File(CUSTOMERSFILE);
            if (!f.exists()) return;

            Scanner reader = new Scanner(f);
            customerCount = 0;

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    customerId[customerCount] = Integer.parseInt(parts[0]);
                    customerName[customerCount] = parts[1];
                    customerPhone[customerCount] = parts[2];
                    customerCount++;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    static void saveSalesToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(SALESFILE));
            for (int i = 0; i < saleCount; i++) {
                writer.println(saleId[i] + "," +
                        saleCustomerId[i] + "," +
                        saleProductId[i] + "," +
                        saleQuantity[i] + "," +
                        saleAmount[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving sales: " + e.getMessage());
        }
    }

    static void loadSalesFromFile() {
        try {
            File f = new File(SALESFILE);
            if (!f.exists()) return;

            Scanner reader = new Scanner(f);
            saleCount = 0;

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 5) {
                    saleId[saleCount] = Integer.parseInt(parts[0]);
                    saleCustomerId[saleCount] = Integer.parseInt(parts[1]);
                    saleProductId[saleCount] = parts[2];
                    saleQuantity[saleCount] = Integer.parseInt(parts[3]);
                    saleAmount[saleCount] = Double.parseDouble(parts[4]);
                    saleCount++;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading sales: " + e.getMessage());
        }
    }

    static String generateProductId() {
        return "P" + (productCount + 1);
    }

    static int generateCustomerId() {
        return customerCount + 1;
    }

    static int generateSaleId() {
        return saleCount + 1;
    }

    static void changeManagerPassword() {
        System.out.println("\n--- Change Manager Password ---");
        System.out.print("Enter current password: ");
        String oldPass = sc.nextLine();

        if (!oldPass.equals(managerPassword)) {
            System.out.println("Incorrect current password!");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass = sc.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPass = sc.nextLine();

        if (!newPass.equals(confirmPass)) {
            System.out.println("Passwords do not match!");
            return;
        }

        managerPassword = newPass;
        saveManagerCredentials();
        System.out.println("Password changed successfully!");
    }
}