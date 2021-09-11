public void serveCruises(Cruise[] cruises) {
    Loader[] loaders = new
    Loader[cruises.length];

     

    for(int i = 0; i < cruises.length; i++) {
       //Check each cruise require how many
       //loaders
       int numOfLoadersRequired =
       cruises[i].getNumOfLoadersRequired();
       int loadCounter;
        //create loaders required for the
        //specific cruise
        
        for(int j = 0; j < numOfLoadersRequired; j++) {
            if(loaders[loadCounter] == null) {
                loaders[j] = new Loader(j+1, cruises[i]);    
                System.out.println(loaders[j].toString());
            }
        }
        loadCounter++;
    }
}
