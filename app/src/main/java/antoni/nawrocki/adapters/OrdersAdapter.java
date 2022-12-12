package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.Context;
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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> orders = new ArrayList<>();

    public OrdersAdapter(Context context, ArrayList<HashMap<String, String>> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        holder.courseName.setText(orders.get(position).get(Courses.COLUMN_NAME_TITLE));
        holder.orderDate.setText(orders.get(position).get(Orders.COLUMN_NAME_DATE));
        holder.price.setText(orders.get(position).get(Orders.COLUMN_NAME_PRICE) + " PLN");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView orderDate;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.order_list_item_course_name);
            orderDate = itemView.findViewById(R.id.order_lsit_item_order_date);
            price = itemView.findViewById(R.id.order_lsit_item_price);
        }
    }
}
