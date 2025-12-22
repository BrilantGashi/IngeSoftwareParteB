package model;

import java.util.ArrayList;

public class ListaFruitoreModel {
    private ArrayList<FruitoreModel> listaFruitore;

    public ListaFruitoreModel() {
        this.listaFruitore = new ArrayList<>();
    }

    public void aggiungiFruitore(FruitoreModel fruitore) {
        listaFruitore.add(fruitore);
    }

    public FruitoreModel cercaFruitore(String nickname) {
        for (FruitoreModel f : listaFruitore) {
            if (f.getCredenziali().getUsername().equalsIgnoreCase(nickname)) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<FruitoreModel> getListaFruitore() {
        return listaFruitore;
    }
}
