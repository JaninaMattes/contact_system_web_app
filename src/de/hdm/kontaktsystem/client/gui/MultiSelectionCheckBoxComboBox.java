package de.hdm.kontaktsystem.client.gui;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/*
 * Quelle: 
 * https://gist.github.com/branflake2267/f94952884d3808809170
 */

public class MultiSelectionCheckboxComboBox<M extends HasChecked> extends Composite {

  // cell for list view checkbox
  protected class CheckBoxCellHasCell implements HasCell<M, Boolean> {
    private CheckBoxCell cell = new CheckBoxCell();

    @Override
    public Cell<Boolean> getCell() {
      return cell;
    }

    @Override
    public FieldUpdater<M, Boolean> getFieldUpdater() {
      return new FieldUpdater<M, Boolean>() {
        @Override
        public void update(int index, M object, Boolean value) {
          object.setChecked(value);
        }
      };
    }

    @Override
    public Boolean getValue(M object) {
      return object.getChecked();
    }
  }

  // cell for list view label
  protected class TextHasCell implements HasCell<M, String> {
    private TextCell cell = new TextCell();

    @Override
    public Cell<String> getCell() {
      return cell;
    }

    @Override
    public FieldUpdater<M, String> getFieldUpdater() {
      return new FieldUpdater<M, String>() {
        @Override
        public void update(int index, M object, String value) {
          object.setName(value);
        }
      };
    }

    @Override
    public String getValue(M object) {
      return object.getName();
    }
  }

  // override the checkbox, so it won't collapse during selection of item
  protected class CheckBoxComboBoxCell<M extends HasChecked> extends ComboBoxCell<M> {
    private boolean ignoreCollapse;

    public CheckBoxComboBoxCell(ListStore<M> store, LabelProvider<? super M> labelProvider, ListView<M, ?> view) {
      super(store, labelProvider, view);
    }

    @Override
    protected void onSelect(M item) {
      item.setChecked(!item.getChecked());
      store.update(item);

      ignoreCollapse = true;
      super.onSelect(item);
      ignoreCollapse = false;
    }

    @Override
    public void collapse(com.google.gwt.cell.client.Cell.Context context, XElement parent) {
      if (!ignoreCollapse) {
        super.collapse(context, parent);
      }
    }
  }

  protected class MultiSelectionComboBox<M extends HasChecked> extends ComboBox<M> {
    public MultiSelectionComboBox(ComboBoxCell<M> cell) {
      super(cell);
    }
  }

  protected MultiSelectionComboBox<M> combo;
  protected ListStore<M> store;
  private LabelProvider<M> labelProvider;

  public MultiSelectionCheckboxComboBox() {
    initWidget(createCombo());
  }

  protected MultiSelectionComboBox<M> createCombo() {
    ModelKeyProvider<M> modelKeyProvider = new ModelKeyProvider<M>() {
      @Override
      public String getKey(M item) {
        return item.getId();
      }
    };

    store = new ListStore<M>(modelKeyProvider);

    // when a selection occurs, draw out this label in the input
    labelProvider = new LabelProvider<M>() {
      @Override
      public String getLabel(M item) {
        String label = "";
        List<M> datas = store.getAll();
        for (M data : datas) {
          if (data.getChecked()) {
            if (label.length() > 0) {
              label += ", ";
            }
            label += data.getName();
          }
        }
        return label;
      }
    };

    List<HasCell<M, ?>> cells = new ArrayList<HasCell<M, ?>>();
    cells.add(new CheckBoxCellHasCell());
    cells.add(new TextHasCell());

    // add two cells to layout a checkbox and label - [] label
    CompositeCell<M> listViewCell = new CompositeCell<M>(cells) {
      protected <X> void render(Context context, M value, SafeHtmlBuilder sb, HasCell<M, X> hasCell) {
        // draw the cells in a line
        Cell<X> cell = hasCell.getCell();
        sb.appendHtmlConstant("<span style='display: inline-block; vertical-align:middle;'>");
        cell.render(context, hasCell.getValue(value), sb);
        sb.appendHtmlConstant("</span>");
      }
    };

    ListView<M, M> listView = new ListView<M, M>(store, new IdentityValueProvider<M>("M"), listViewCell);

    CheckBoxComboBoxCell<M> cell = new CheckBoxComboBoxCell<M>(store, labelProvider, listView);
    combo = new MultiSelectionComboBox<M>(cell);
    combo.setTriggerAction(TriggerAction.ALL);

    return combo;
  }

  public void setValue(List<M> value) {
    store.addAll(value);

    String label = labelProvider.getLabel(null);
    InputElement input = combo.getCell().getInputElement(combo.getElement());
    input.setValue(label);
  }

  public List<M> getValue() {
    List<M> all = store.getAll();
    List<M> some = new ArrayList<M>();
    for (M item : all) {
      if (item.getChecked()) {
        some.add(item);
      }
    }
    return some;
  }

}