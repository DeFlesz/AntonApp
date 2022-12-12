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

public class OrderOptionsAdapter extends RecyclerView.Adapter<OrderOptionsAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> options = new ArrayList<>();

    public OrderOptionsAdapter(ArrayList<HashMap<String, String>> options){
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
        holder.title.setText(options.get(position).get(CoursesOptions.COLUMN_NAME_TITLE));
        holder.description.setText(options.get(position).get(CoursesOptions.COLUMN_NAME_DESCRIPTION));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

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
