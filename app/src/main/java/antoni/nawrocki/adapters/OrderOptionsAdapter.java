package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.db.DBReaderContract;
import antoni.nawrocki.models.CourseOption;

/**
 * Adapter for OrderOptions
 */
public class OrderOptionsAdapter extends RecyclerView.Adapter<OrderOptionsAdapter.ViewHolder> {
    private ArrayList<CourseOption> options = new ArrayList<>();

    /**
     * Contructor for OrderOptions Adapter
     * @param options ArrayList of CourseOption
     */
    public OrderOptionsAdapter(ArrayList<CourseOption> options){
        this.options = options;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_option_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderOptionsAdapter.ViewHolder holder, int position) {
        holder.title.setText(options.get(position).getTitle());
        holder.description.setText(options.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    /**
     * ViewHolder for OrderOptions recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.order_option_title);
            description = itemView.findViewById(R.id.order_option_description);
        }
    }
}
