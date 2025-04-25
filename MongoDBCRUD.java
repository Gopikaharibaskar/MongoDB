package mongoDBExample;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.Scanner;

public class MongoDBCRUD {
    public static void main(String[] args) {
        // Connect to MongoDB server
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("mydatabase");
            MongoCollection<Document> collection = database.getCollection("products");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        System.out.print("Enter Product Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Product Price: ");
                        float price = scanner.nextFloat();
                        scanner.nextLine(); 
                        System.out.print("Enter Product Brand: ");
                        String brand = scanner.nextLine();

                        Document product = new Document("name", name)
                                .append("price", price)
                                .append("brand", brand);
                        collection.insertOne(product);
                        System.out.println("Inserted: " + product.toJson());
                        break;

                    case 2:
                        System.out.println("\nAll Products:");
                        try (MongoCursor<Document> cursor = collection.find().iterator()) {
                            while (cursor.hasNext()) {
                                System.out.println(cursor.next().toJson());
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter Product Name to Update: ");
                        String updateName = scanner.nextLine();
                        System.out.print("Enter New Product Price: ");
                        float newPrice = scanner.nextFloat();
                        scanner.nextLine(); 
                        collection.updateOne(Filters.eq("name", updateName),
                                new Document("$set", new Document("price", newPrice)));
                        System.out.println("Updated " + updateName + "'s price to " + newPrice);
                        break;

                    case 4:
                        // DELETE: Delete a product
                        System.out.print("Enter Product Name to Delete: ");
                        String deleteName = scanner.nextLine();
                        collection.deleteOne(Filters.eq("name", deleteName));
                        System.out.println("Deleted product: " + deleteName);
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice, try again!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
