package upworksolutions.themagictricks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.MagicCategory;

public class MagicCategoryAdapter extends RecyclerView.Adapter<MagicCategoryAdapter.CategoryViewHolder> {
    private List<MagicCategory> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(MagicCategory category);
    }

    public MagicCategoryAdapter(List<MagicCategory> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_magic_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        MagicCategory category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private TextView categoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryName = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCategoryClick(categories.get(position));
                }
            });
        }

        void bind(MagicCategory category) {
            categoryName.setText(category.getName());
            
            // Set category icon based on category ID
            int iconResId = getCategoryIcon(category.getId());
            categoryIcon.setImageResource(iconResId);
        }

        private int getCategoryIcon(int categoryId) {
            switch (categoryId) {
                case 1: // Card Magic
                    return R.drawable.ic_card_magic;
                case 2: // Coin Magic
                    return R.drawable.ic_coin_magic;
                case 5: // Mentalism & Mind-Reading
                    return R.drawable.ic_mentalism;
                case 6: // Illusions
                    return R.drawable.ic_illusions;
                case 8: // Close-up Magic
                    return R.drawable.ic_close_up_magic;
                case 3: // Rope Magic
                    return R.drawable.ic_rope_magic;
                case 7: // Stage Magic
                    return R.drawable.ic_stage_magic;
                case 4: // Street Magic
                    return R.drawable.ic_street_magic;
                default:
                    return R.drawable.ic_magic_wand;
            }
        }
    }
} 