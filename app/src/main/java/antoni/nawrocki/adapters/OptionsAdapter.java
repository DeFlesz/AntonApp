package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.db.DBReaderContract;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> options;
    private ArrayList<String> selectedOptionIDs = new ArrayList<>();
    private double finalPrice;
    private TextView priceField;

    public ArrayList<String> getSelectedOptionIDs() {
        return selectedOptionIDs;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public OptionsAdapter(ArrayList<HashMap<String, String>> options, TextView textView, double basePrice) {
        this.options = options;
        this.finalPrice = basePrice;
        this.priceField = textView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(options.get(position).get(CoursesOptions.COLUMN_NAME_TITLE));
        holder.description.setText(options.get(position).get(CoursesOptions.COLUMN_NAME_DESCRIPTION));
        holder.price.setText("Cena: " + options.get(position).get(CoursesOptions.COLUMN_NAME_PRICE));

        holder.itemView.setOnClickListener(v -> {
            holder.optionButton.performClick();
        });

        holder.optionButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            String optionID = options.get(position).get(CoursesOptions._ID);
            double price = Double.parseDouble(options.get(position).get(CoursesOptions.COLUMN_NAME_PRICE));
            if (isChecked) {
                selectedOptionIDs.add(optionID);
                finalPrice += price;
            } else {
                selectedOptionIDs.remove(optionID);
                finalPrice -= price;
            }
            priceField.setText(""+finalPrice);
//            Toast.makeText(compoundButton.getContext(), selectedOptionIDs.toString() + ", finalna cena: " + finalPrice, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView price;
        CheckBox optionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.option_item_title);
            description = itemView.findViewById(R.id.option_item_description);
            price = itemView.findViewById(R.id.option_item_price);
            optionButton = itemView.findViewById(R.id.option_item_checkbox);
        }
    }
}
