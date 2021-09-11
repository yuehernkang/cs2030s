public void serveCruises(Cruise[] cruises) {
    Loader[] loaders = new Loader[cruises.length];

    for(int i = 0; i < cruises.length; i++) {
       //Check each cruise require how many loaders
       
       int numOfLoadersRequired = cruises[i].getNumOfLoadersRequired();
       
       int loadCounter = 0;
        //create loaders required for the
        //specific cruise
        
        for(int j = 0; j < numOfLoadersRequired; j++) {
            for (int k = 0; i < loadCounter; k++) {
                if(loaders[loadCounter].canServe()){
                    
                }
            }
            if(loaders[loadCounter].canServe()) {
                loaders[j] = new Loader(j+1, cruises[i]);    
                System.out.println(loaders[j].toString());
            }

        }
        loadCounter++;
    }
}
