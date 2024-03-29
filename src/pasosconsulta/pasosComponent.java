package pasosconsulta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vista.ConsultasComponent;

public class pasosComponent implements ActionListener{

    private pasosTemplate pasosTemplate;
    private ConsultasComponent consultasComponent;

    public pasosComponent(ConsultasComponent consultasComponent){
        this.pasosTemplate = new pasosTemplate(this);
        this.consultasComponent = consultasComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       this.consultasComponent.mostrarTablas(e.getActionCommand().trim());
        
    }

    public pasosTemplate getpasosTemplate(){
        return pasosTemplate;
    }

}
