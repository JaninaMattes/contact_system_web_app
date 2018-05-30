package test;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

public class MultipleChoiceComboBoxTestKlasse {
	
	
	public void multiComboBox() { 

	Data data0 = new Data("0", "zero", true);
    Data data1 = new Data("1", "one", false);
    Data data2 = new Data("2", "two", true);
    Data data3 = new Data("3", "three", false);
    Data data4 = new Data("4", "four", true);

    List<Data> selected = new ArrayList<Data>();
    selected.add(data0);
    selected.add(data1);
    selected.add(data2);
    selected.add(data3);
    selected.add(data4);

    final MultiSelectionCheckboxComboBox<Data> combo = new MultiSelectionCheckboxComboBox<Data>();
    combo.setValue(selected);

    RootPanel.get("Editor").add(combo);

    TextButton button = new TextButton("Get Value");
    button.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        Info.display("value=", combo.getValue() + "");
      }
    });
    RootPanel.get().add(button);
  }

  public class Data implements HasChecked {
    private String id;
    private String name;
    private boolean checked;

    public Data(String id, String name, boolean checked) {
      this.id = id;
      this.name = name;
      this.checked = checked;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean getChecked() {
      return checked;
    }

    public void setChecked(boolean checked) {
      this.checked = checked;
    }

    @Override
    public String toString() {
      return name;
    }
  }
  
}
  
}
