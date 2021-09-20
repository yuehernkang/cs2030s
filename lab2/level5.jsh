import java.util.ArrayList; // import the ArrayList class

public void serveCruises(Cruise[] cruises) {
    Loader[] loaders = new Loader[999];
    int numOfLoadersPurchased = 0;
    Loader[] loaderAvailable = new Loader[99];

    for(int i = 0; i < cruises.length; i++) {
        //Check each cruise require how many loaders
        int numOfLoadersRequired = cruises[i].getNumOfLoadersRequired();
        //Array containing loaders that are available
        ArrayList<Loader> loaderAvailableList = new ArrayList<Loader>();

        //loop through number of loaders required
        for(int j = 0; j < numOfLoadersRequired - 1; j++){
            //For each cruise, check through the inventory of existing loaders, 
            //starting from the loader first purchased, and so on;
            if(numOfLoadersPurchased == 0) {
                loaders[j]  = new Loader(numOfLoadersPurchased+1, cruises[i]);
                numOfLoadersPurchased++;
                System.out.println(loaders[j].toString());
            }
            //get list of available loaders
            for(int k = 0; k < numOfLoadersPurchased; k++ ) {
                System.out.println("numOfLoadersPurchased " + numOfLoadersPurchased);
                if(loaders[k].canServe(cruises[i])) {
                    loaderAvailableList.add(loaders[k]);
                }
                else{
                    System.out.println("loader cannot serve: " + k);
                }
            }
            //LOOP THROUGH THE ARRAY LIST
            for(int d = 0; d < loaderAvailableList.size() - 1; d++) {
                System.out.println("available loader found: " + loaderAvailableList.get(d).toString());
                loaders[j]  = new Loader(loaderAvailableList.get(d).getIdentifier(), cruises[i]);
                numOfLoadersPurchased++;
                System.out.println(loaders[j].toString());
            }
            // check if enough loaders or not
            if(loaderAvailableList.size() < numOfLoadersRequired) {
                //purchase new loader 
                loaders[j]  = new Loader(numOfLoadersPurchased+1, cruises[i]);
                numOfLoadersPurchased++;
                System.out.println(loaders[j].toString());
            }

            //purchase new loader 
            // loaders[j]  = new Loader(numOfLoadersPurchased+1, cruises[i]);
            // numOfLoadersPurchased++;
            // System.out.println(loaders[j].toString());

        }


    }
}
