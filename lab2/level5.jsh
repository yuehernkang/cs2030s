public void serveCruises(Cruise[] cruises) {
    Loader[] loaders = new Loader[999];
    int numOfLoadersOut = 1;

    for(int i = 0; i < cruises.length; i++) {
        //Check each cruise require how many loaders
        int numOfLoadersRequired = cruises[i].getNumOfLoadersRequired();
        
        //create loaders required for the specific cruise
        for(int j = 0; j < numOfLoadersRequired; j++) {
            //check if there are any available loaders
            for(int k = 0; k < numOfLoadersOut; k++) {
                if(loaders[k].canServe(cruises[i])){
                    loaders[k] = new Loader(k+1, cruises[i]);    
                    System.out.println(loaders[k].toString());
                    numOfLoadersOut++;
                }
            }
        }
    }
}
