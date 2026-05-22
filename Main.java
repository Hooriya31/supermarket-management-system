import java.util.*;
import java.io.*;

public class Main {
    static String[] productId = new String[500];
    static String[] productName = new String[500];
    static String[] productCategory = new String[500];
    static double[] productPrice = new double[500];
    static int[] productStock = new int[500];
    static String[] productExpiry = new String[500];
    static int productCount = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) 
    {
        System.out.println("Supermarket Management System");
        loadProductsFromFile();
        productManagementMenu();
    }

//======================================================================================

// ======================= HELPER METHODS ========================
static String generateProductId() {
    return "P" + (productCount + 1);
}

static int findProductById(String id) {
    for (int i = 0; i < productCount; i++) {
        if (productId[i].equalsIgnoreCase(id)) {
            return i;
        }
    }
    return -1;
}
static void printProductTableHeader(){
    System.out.printf("%-8s %-18s %-12s %-10s %-8s %-12s\n",
            "ID", "Name", "Category", "Price", "Stock", "Expiry");
    System.out.println("---------------------------------------------------------------");
}
static void printProductRow(int i){
    System.out.printf("%-8s %-18s %-12s %-10.2f %-8d %-12s\n",
            productId[i], productName[i], productCategory[i],
            productPrice[i], productStock[i], productExpiry[i]);
}
static void pause() {
    System.out.println("\nPress Enter to continue...");
    sc.nextLine();
}

// ===================== PRODUCT MENU =====================

static void productManagementMenu(){
    while (true) {
        System.out.println("\n===== PRODUCT MANAGEMENT =====");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Search Products");
        System.out.println("6. Inventory Reports");
        System.out.println("0. Back");
        System.out.print("Enter choice: ");
        int ch = sc.nextInt(); sc.nextLine();

        switch (ch) {
            case 1: addProduct(); 
            break;
            case 2: viewAllProducts();
            break;
            case 3: updateProduct(); 
            break;
            case 4: deleteProduct(); 
            break;
            case 5: searchProduct(); 
            break;
            case 6: inventoryReportsMenu(); 
            break;
            case 0: 
            return;
            default: System.out.println("Invalid choice!");
        }
    }
}

// ===================== CRUD OPERATIONS =====================

// ADD
static void addProduct() {
    if (productCount >= 500){
    System.out.println("Inventory full!");
    return;
    }

    String id=generateProductId();
    System.out.println("\nAdding Product ID = " + id);

    System.out.print("Enter Name: ");
    productName[productCount] = sc.nextLine();

    System.out.print("Enter Category: ");
    productCategory[productCount] = sc.nextLine();

    System.out.print("Enter Price: ");
    productPrice[productCount] = sc.nextDouble();

    System.out.print("Enter Stock: ");
    productStock[productCount] = sc.nextInt(); sc.nextLine();

    System.out.print("Enter Expiry (yyyy-mm-dd): ");
    productExpiry[productCount] = sc.nextLine();

    productId[productCount] = id;
    productCount++;

    saveProductsToFile();
    System.out.println("Product added successfully!");
}

// VIEW
static void viewAllProducts() {
    if (productCount == 0) {
        System.out.println("No products in inventory!");
        return;
    }
    printProductTableHeader();
    for (int i=0; i<productCount; i++) {
        printProductRow(i);
    }
    pause();
}

// UPDATE
static void updateProduct() {
    System.out.print("Enter Product ID to update: ");
    String id = sc.nextLine();
    int index=findProductById(id);
    if (index == -1) {
        System.out.println("Product not found!");
        return;
    }
    System.out.print("New Name: ");
    productName[index] = sc.nextLine();

    System.out.print("New Category: ");
    productCategory[index] = sc.nextLine();

    System.out.print("New Price: ");
    productPrice[index] = sc.nextDouble();

    System.out.print("New Stock: ");
    productStock[index] = sc.nextInt(); sc.nextLine();

    System.out.print("New Expiry: ");
    productExpiry[index] = sc.nextLine();

    saveProductsToFile();
    System.out.println("Product updated!");
}

// DELETE
static void deleteProduct() {
    System.out.print("Enter Product ID to delete: ");
    String id = sc.nextLine();
    int index=findProductById(id);
    if (index == -1) {
        System.out.println("Product not found!");
        return;
    }
    for (int i=index; i<productCount-1; i++) {
        productId[i] = productId[i+1];
        productName[i] = productName[i+1];
        productCategory[i] = productCategory[i+1];
        productPrice[i] = productPrice[i+1];
        productStock[i] = productStock[i+1];
        productExpiry[i] = productExpiry[i+1];
    }
    productCount--;
    saveProductsToFile();
    System.out.println("Product deleted!");
}

// ===================== SEARCH FUNCTIONS =====================

// search menu
static void searchProduct() {
    System.out.println("\nSEARCH OPTIONS:");
    System.out.println("1. Search by Name");
    System.out.println("2. Search by Category");
    System.out.println("3. Search by Price Range");
    System.out.println("4. Recursive Search (Name)");
    System.out.println("0. Back");
    int ch = sc.nextInt(); sc.nextLine();
    switch (ch) {
        case 1: searchProductsByField("name"); 
        break;
        case 2: searchProductsByField("category"); 
        break;
        case 3: priceRangeFilter(); 
        break;
        case 4: recursiveProductSearch(); 
        break;
        case 0: 
        return;
    }
}
// search by name or category
static void searchProductsByField(String field) {
    System.out.print("Enter " + field + " to search: ");
    String key=sc.nextLine().toLowerCase();
    boolean found = false;
    printProductTableHeader();
    for (int i = 0; i < productCount; i++) {
        if ((field.equals("name") && productName[i].toLowerCase().contains(key))||(field.equals("category") && productCategory[i].toLowerCase().contains(key)))
             {
            printProductRow(i);
            found=true;
        }
    }
    if(!found) System.out.println("No matching products found!");
    pause();
}

//recursive search(only name)
static void recursiveProductSearch() {
    System.out.print("Enter name keyword: ");
    String key=sc.nextLine().toLowerCase();
    recursiveSearchHelper(0, key);
    pause();
}
static void recursiveSearchHelper(int index, String key) {
    if (index >= productCount) return;

    if (productName[index].toLowerCase().contains(key)) {
        printProductRow(index);
    }

    recursiveSearchHelper(index+1, key);
}

// ===================== REPORTS MENU =====================

static void inventoryReportsMenu() {
    System.out.println("\n===== INVENTORY REPORTS =====");
    System.out.println("1. Low Stock");
    System.out.println("2. Out of Stock");
    System.out.println("3. Category Wise Count");
    System.out.println("4. Total Inventory Value");
    System.out.println("0. Back");
    int ch=sc.nextInt(); sc.nextLine();
    switch (ch) {
        case 1: showLowStockAlert(); 
        break;
        case 2: showOutOfStock(); 
        break;
        case 3: showCategoryWiseInventory(); 
        break;
        case 4: showTotalInventoryValue(); 
        break;
        case 0: 
        return;
    }
}

//LOW STOCK
static void showLowStockAlert() {
    printProductTableHeader();
    for (int i=0; i<productCount; i++) {
        if (productStock[i] <= 5) {
            printProductRow(i);
        }
    }
    pause();
}

//OUT OF STOCK
static void showOutOfStock() {
    printProductTableHeader();
    for (int i = 0; i < productCount; i++) {
        if (productStock[i] == 0) {
            printProductRow(i);
        }
    }
    pause();
}

//CATEGORY WISE COUNT
static void showCategoryWiseInventory() {
    System.out.println("\nCATEGORY WISE PRODUCT COUNT:");
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


//TOTAL VALUE
static void showTotalInventoryValue(){
    double total = 0;
    for (int i=0; i<productCount; i++) {
        total += productPrice[i]*productStock[i];
    }
    System.out.printf("Total Inventory Value = Rs %.2f\n", total);
    pause();
}

// PRICE RANGE FILTER
static void priceRangeFilter() {
    System.out.print("Min price: ");
    double min=sc.nextDouble();
    System.out.print("Max price: ");
    double max=sc.nextDouble();
    sc.nextLine();
    printProductTableHeader();
    for (int i=0; i<productCount; i++) {
        if (productPrice[i] >= min && productPrice[i] <= max) {
            printProductRow(i);
        }
    }
    pause();
}

// ===================== FILE HANDLING =====================

static void saveProductsToFile() 
{
    try {
        PrintWriter pw=new PrintWriter(new FileWriter("products.csv"));
        for (int i = 0; i < productCount; i++) {
            pw.println(productId[i] + "," +
                       productName[i] + "," +
                       productCategory[i] + "," +
                       productPrice[i] + "," +
                       productStock[i] + "," +
                       productExpiry[i]);
        }
        pw.close();
        System.out.println("Products saved to file.");
    }
    catch (Exception e)
     {
        System.out.println("Error saving products.");
    }
}

static void loadProductsFromFile()
{
    try {
        BufferedReader br=new BufferedReader(new FileReader("products.csv"));
        String line;
        productCount=0;
        while ((line = br.readLine()) != null) {
            if (productCount >= 500) 
                break;
            String[] data = line.split(",");
            productId[productCount] = data[0];
            productName[productCount] = data[1];
            productCategory[productCount] = data[2];
            productPrice[productCount] = Double.parseDouble(data[3]);
            productStock[productCount] = Integer.parseInt(data[4]);
            productExpiry[productCount] = data[5];
            productCount++;
        }
        br.close();
    }
    catch (Exception e) 
    {
        System.out.println("Products file not found.Starting fresh.");
    }
  }
}
