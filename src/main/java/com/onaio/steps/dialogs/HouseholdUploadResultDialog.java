package com.onaio.steps.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.model.UploadResult;

import java.util.Collections;
import java.util.List;

public class HouseholdUploadResultDialog extends DialogFragment {

    private View rootView;
    private final List<UploadResult> uploadResults;

    public HouseholdUploadResultDialog(List<UploadResult> uploadResults) {
        this.uploadResults = uploadResults;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_household_upload_results, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Collections.sort(uploadResults, (ur1, ur2) -> Boolean.compare(ur1.isSuccess(), ur2.isSuccess()));

        UploadResultAdapter uploadResultAdapter = new UploadResultAdapter(uploadResults);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_upload_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(uploadResultAdapter);

        rootView.findViewById(R.id.btn_ok).setOnClickListener(v -> dismiss());
    }

    public static class UploadResultAdapter extends RecyclerView.Adapter<UploadResultAdapter.ViewHolder> {

        private final List<UploadResult> uploadResults;

        public UploadResultAdapter(List<UploadResult> uploadResults) {
            this.uploadResults = uploadResults;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_household_upload_result, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UploadResult uploadResult = uploadResults.get(position);
            holder.bind(uploadResult);
        }

        @Override
        public int getItemCount() {
            return uploadResults.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final ImageView ivResult;
            private final TextView formTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivResult = itemView.findViewById(R.id.iv_result);
                formTitle = itemView.findViewById(R.id.form_title);
            }

            public void bind(UploadResult uploadResult) {
                ivResult.setImageDrawable(ContextCompat.getDrawable(ivResult.getContext(), uploadResult.isSuccess() ? R.drawable.ic_baseline_check_24 : R.drawable.ic_baseline_close_24));
                formTitle.setText(uploadResult.getFormTitle());
            }
        }
    }
}
