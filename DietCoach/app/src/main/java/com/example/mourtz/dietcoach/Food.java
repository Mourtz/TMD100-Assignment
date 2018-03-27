package com.example.mourtz.dietcoach;

public class Food {
    enum Type {
        FRUITS, VEGETABLES, GRAINS, DAIRY, MEAT, SEAFOOD
    }

    public static final Food[] foods = new Food[]{
        //--------------------------------------- Meat and Poultry ---------------------------------------
        new Food("Bacon", Type.MEAT, 16,95, 4, 1, 8,7),
        new Food("Beef", Type.MEAT, 85,245, 23, 0, 16,15),
        new Food("Steak", Type.MEAT, 85,330, 20, 0, 27,25),
        new Food("Chicken(Boiled)", Type.MEAT, 85,185, 23, 0, 9,7),
        new Food("Chicken(Fried)", Type.MEAT, 85,245, 25, 0, 15,11),
        new Food("Chicken(Roasted)", Type.MEAT, 100,290, 25, 0, 20,16),
        new Food("Chicken Livers(Fried)", Type.MEAT, 100,140, 22, 2, 14,12),
        new Food("Duck", Type.MEAT, 100,370, 16, 0, 28,0),
        new Food("Turkey(Roasted)", Type.MEAT, 100,265, 27, 0, 15,0),
        new Food("Veal", Type.MEAT, 85,185, 23, 0, 9,8),

        //--------------------------------------- Dairy ---------------------------------------
        new Food("Cow's Milk(Whole)", Type.DAIRY, 976,660, 32, 48, 40,36),
        new Food("Buttermilk", Type.DAIRY, 246,127, 9, 13, 5,4),
        new Food("Powdered Milk", Type.DAIRY, 103,515, 27, 39, 28,24),
        new Food("Yogurt", Type.DAIRY, 250,128, 18, 13, 4,3),
        new Food("Cream Cheese", Type.DAIRY, 28,105, 2, 1, 11,10),
        new Food("Eggs(Boiled)", Type.DAIRY, 100,150, 12, 0, 12,10),
        new Food("Eggs(Scrambled or Fried)", Type.DAIRY, 128,220, 13, 0, 16,14),

        //--------------------------------------- Fruits ---------------------------------------
        new Food("Apples(Raw)", Type.FRUITS, 130,70, 0, 18, 0,0),
        new Food("Apples(Juice)", Type.FRUITS, 250,125, 0, 34, 0,0),
        new Food("Apricots(Dried)", Type.FRUITS, 75,220, 4, 50, 0,0),
        new Food("Apricots(Fresh)", Type.FRUITS, 114,55, 1, 14, 0,0),
        new Food("Apricots(Nectar or Juice)", Type.FRUITS, 250,140, 1, 36, 0,0),
        new Food("Banana", Type.FRUITS, 150,85, 1, 23, 0,0),
        new Food("Blackberries(Fresh)", Type.FRUITS, 144,85, 2, 19, 0,0),
        new Food("Blackberries(Canned)", Type.FRUITS, 250,245, 2, 65, 0,0),
        new Food("Cherries(Fresh)", Type.FRUITS, 114,90, 2, 22, 0,0),
        new Food("Grapefruit(Fresh)", Type.FRUITS, 285,50, 1, 14, 0,0),
        new Food("Grapefruit(Canned)", Type.FRUITS, 250,170, 1, 44, 0,0),
        new Food("Grapefruit(Juice)", Type.FRUITS, 250,100, 1, 24, 0,0),
        new Food("Oranges(Fresh)", Type.FRUITS, 180,60, 2, 16, 0,0),
        new Food("Oranges(Juice)", Type.FRUITS, 250,112, 2, 25, 0,0),
        new Food("Papaya(Juice)", Type.FRUITS, 200,75, 1, 18, 0,0),
        new Food("Peaches", Type.FRUITS, 114,35, 1, 10, 0,0),

        //--------------------------------------- Vegetables ---------------------------------------
        new Food("Asparagus", Type.VEGETABLES, 96,18, 1, 3, 0,0),
        new Food("Beans", Type.VEGETABLES, 125,25, 1, 6, 0,0),
        new Food("Bean Sprouts", Type.VEGETABLES, 50,17, 1, 3, 0,0),
        new Food("Broccoli", Type.VEGETABLES, 150,45, 5, 8, 0,0),
        new Food("Corn", Type.VEGETABLES, 100,92, 3, 21, 1,0),
        new Food("Eggplant", Type.VEGETABLES, 180,30, 2, 9, 0,0),
        new Food("Kale", Type.VEGETABLES, 110,45, 4, 8, 0,0),
        new Food("Mushrooms", Type.VEGETABLES, 120,12, 2, 4, 0,0),
        new Food("Peas", Type.VEGETABLES, 100,66, 3, 13, 0,0),

        //--------------------------------------- Seafood ---------------------------------------
        new Food("Clams", Type.SEAFOOD, 85,87, 12, 2, 0,0),
        new Food("Fish Sticks", Type.SEAFOOD, 112,200, 19, 8, 10,5),
        new Food("Lobster", Type.SEAFOOD, 100,92, 18, 0, 1,0),
        new Food("Salmon", Type.SEAFOOD, 85,120, 17, 0, 5,1),
        new Food("Scallops", Type.SEAFOOD, 100,104, 18, 10, 8,0),
        new Food("Shrimp", Type.SEAFOOD, 85,110, 23, 0, 1,0),
        new Food("Tuna", Type.SEAFOOD, 85,170, 25, 0, 7,3),

        //--------------------------------------- Grains ---------------------------------------
        new Food("Biscuits", Type.GRAINS, 38,130, 3, 18, 4,3),
        new Food("Wheat Bread", Type.GRAINS, 23,60, 2, 12, 1,1),
        new Food("Whole Wheat Bread", Type.GRAINS, 23,55, 2, 11, 1,0),
        new Food("Corn Bread", Type.GRAINS, 50,100, 3, 15, 4,2),
        new Food("Crackers", Type.GRAINS, 14,55, 1, 10, 1,0),
        new Food("Noodles", Type.GRAINS, 160,200, 7, 37, 2,2),
        new Food("Pizza", Type.GRAINS, 75,180, 8, 23, 6,5),
        new Food("Spaghetti with meat sauce", Type.GRAINS, 250,285, 13, 35, 10,6),
        new Food("Spaghetti with with tomatoes and cheese", Type.GRAINS, 250,210, 6, 36, 5,3),
        new Food("Waffles", Type.GRAINS, 75,240, 8, 30, 9,1),

    };

    String name;
    Type type;
    int grams;
    int calories;
    int protein;
    int carbohydrates;
    int fat;
    int saturated_fat;

    public Food(String _name, Type _type, int _grams, int _cal, int _protein, int _carbohydrates, int _fat, int _saturated_fat){
        grams = _grams;
        name = _name;
        type = _type;
        calories = _cal;
        protein = _protein;
        carbohydrates = _carbohydrates;
        fat = _fat;
        saturated_fat = _saturated_fat;
    }
}

