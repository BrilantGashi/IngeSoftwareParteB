package controller;

import model.DataModel;
import view.DataView;

public class DataController {
    private DataView view;

    public DataController(DataView view) {
        this.view = view;
    }

    public DataModel scegliDataPerMese(int mese) {
        DataModel data = view.leggiData(mese);
        view.mostraData(data);
        return data;
    }
    
    public DataModel chiediDataAllUtente(String promptGiorno, String promptMese) {
        DataModel data = null;
        do {
            try {
                data = view.leggiDataDaUtente(promptGiorno, promptMese);
            } catch (Exception e) {
                view.mostraMessaggioErrore(e.getMessage());
                data = null;
            }
        } while (data == null);
        return data;
    }


}
