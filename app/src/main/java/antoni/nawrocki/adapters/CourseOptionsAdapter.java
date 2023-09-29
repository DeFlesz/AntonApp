package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.models.CourseOption;

/**
 * Adapter for the CourseOptions Recycler view
 */
public class CourseOptionsAdapter extends RecyclerView.Adapter<CourseOptionsAdapter.ViewHolder> {
    private ArrayList<CourseOption> options;
    private ArrayList<String> selectedOptionIDs = new ArrayList<>();
    private double finalPrice;
    private TextView priceField;

    /**
     * Finds the selected options
     * @return ArrayList of selected options
     */
    public ArrayList<String> getSelectedOptionIDs() {
        return selectedOptionIDs;
    }

    /**
     * Calculates the final price for the Course
     * @return Final price for the course
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /**
     * Contructor for the adapter
     * @param options ArrayList of CourseOption containing all options for given course
     * @param textView TextView containing the price
     * @param basePrice Base price of the course
     */
    public CourseOptionsAdapter(ArrayList<CourseOption> options, TextView textView, double basePrice) {
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
        holder.title.setText(options.get(position).getTitle());
        holder.description.setText(options.get(position).getDescription());
        holder.price.setText((int) options.get(position).getPrice() + " PLN");

        holder.itemView.setOnClickListener(v -> {
            holder.optionButton.performClick();
        });

        holder.optionButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            String optionID = options.get(position).getId();
            double price = options.get(position).getPrice();
            if (isChecked) {
                selectedOptionIDs.add(optionID);
                finalPrice += price;
            } else {
                selectedOptionIDs.remove(optionID);
                finalPrice -= price;
            }
            priceField.setText(""+Integer.parseInt(String.valueOf(Math.round(finalPrice))));
//            Toast.makeText(compoundButton.getContext(), selectedOptionIDs.toString() + ", finalna cena: " + finalPrice, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    /**
     * ViewHolder for the CourseOption recycler view
     */
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
