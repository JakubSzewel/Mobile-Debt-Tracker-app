package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private static List<Friend> friendList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    private OnHistoryButtonClickListener onHistoryButtonClickListener;

    public interface OnHistoryButtonClickListener {
        void onHistoryButtonClick(Friend friend);
    }

    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }
    public FriendAdapter(Context context, List<Friend> friendList, OnItemClickListener onItemClickListener, OnHistoryButtonClickListener onHistoryButtonClickListener) {
        this.context = context;
        this.friendList = friendList;
        this.onItemClickListener = onItemClickListener;
        this.onHistoryButtonClickListener = onHistoryButtonClickListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.textViewFriendName.setText(friend.getName());

        holder.textViewPhoneNumber.setText("Telefon: " + friend.getPhoneNumber());

        if (friend.getAmountOwed() > 0) {
            holder.textViewAmountOwed.setText(String.format("Należne: %.2fzł", friend.getAmountOwed()));
            holder.textViewAmountOwed.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.positiveColor));
        }
        else if (friend.getAmountOwed() == 0){
            holder.textViewAmountOwed.setText(String.format("Należne: %.2fzł", friend.getAmountOwed()));
            holder.textViewAmountOwed.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }
        else {
            holder.textViewAmountOwed.setText(String.format("Do oddania: %.2fzł", friend.getAmountOwed()));
            holder.textViewAmountOwed.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.negativeColor));
        }
    }

    @Override
    public int getItemCount() {
        return friendList != null ? friendList.size() : 0;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFriendName;
        public TextView textViewAmountOwed;
        public TextView textViewPhoneNumber;
        public Button buttonHistory;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFriendName = itemView.findViewById(R.id.textViewFriendName);
            textViewAmountOwed = itemView.findViewById(R.id.textViewAmountOwed);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);

            Button btnRemoveFriend = itemView.findViewById(R.id.btnRemoveFriend);
            btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRemoveConfirmationDialog(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(friendList.get(position));
                    }
                }
            });
            buttonHistory = itemView.findViewById(R.id.btnHistory);
            buttonHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onHistoryButtonClickListener != null) {
                        onHistoryButtonClickListener.onHistoryButtonClick(friendList.get(position));
                    }
                }
            });
        }
        private void showRemoveConfirmationDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Czy na pewno chczesz usunąć tę osobę ze znajomych?")
                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User confirmed, remove the friend
                            removeFriend(position);
                        }
                    })
                    .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User canceled, do nothing
                        }
                    })
                    .create()
                    .show();
        }
        private void removeFriend(int position) {
            friendList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, friendList.size());
            FriendsManager.saveFriends(itemView.getContext(), friendList);
        }


    }
}
