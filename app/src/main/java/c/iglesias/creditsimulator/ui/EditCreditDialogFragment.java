package c.iglesias.creditsimulator.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.common.Utils;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.CreditEntity;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

import static c.iglesias.creditsimulator.common.Utils.getDetailFromCuota;
import static c.iglesias.creditsimulator.common.Utils.getDetailFromPlazo;
import static c.iglesias.creditsimulator.common.Utils.getPlazo;


public class EditCreditDialogFragment extends DialogFragment {

    private View view;
    private EditText edtName, edtEntidad, edtPlazo, edtValor, edtTasa, edtCuota;
    private Button btnGuardar, btnCancelar;
    private Spinner spinner;
    private CreditEntity entity;

    private CreditViewModel mViewModel;

    private int cvCuota;

    public EditCreditDialogFragment(CreditEntity entity , int cuota) {

        this.entity = entity;
        this.cvCuota= cuota;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_credit_dialog_fragment, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getActivity())
                .get(CreditViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View title = getActivity().getLayoutInflater().inflate(R.layout.title_dialog, null);
        ((TextView) title.findViewById(R.id.idTxtTitle)).setText(R.string.str_title_edit_credit);
        builder.setCustomTitle(title);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_credit_dialog_fragment, null);

        edtPlazo = view.findViewById(R.id.idEdtPlazo);
        edtValor = view.findViewById(R.id.idEdtValor);
        edtTasa = view.findViewById(R.id.idEdtTasa);
        edtCuota = view.findViewById(R.id.idEdtCuota);

        spinner = view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        edtPlazo.setEnabled(false);
                        edtCuota.setEnabled(true);
                        break;
                    case 2:
                        edtPlazo.setEnabled(true);
                        edtCuota.setEnabled(false);
                        break;
                    default:
                        edtPlazo.setEnabled(false);
                        edtCuota.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtValor.addTextChangedListener(new CustomTextWatcher(edtValor));
        edtCuota.addTextChangedListener(new CustomTextWatcher(edtCuota));
        edtEntidad = view.findViewById(R.id.idEdtEntidad);
        edtName = view.findViewById(R.id.idEdtNameCretir);
        btnCancelar = view.findViewById(R.id.idBtnCancelar);
        btnGuardar = view.findViewById(R.id.idBtnGuardar);



        edtPlazo.setText(String.valueOf(entity.getDuration()));
        edtValor.setText(Utils.formatNumber(String.valueOf(entity.getValue()), false));
        edtTasa.setText(String.valueOf(entity.getTasa()));
        edtEntidad.setText(entity.getNameBank());
        edtName.setText(entity.getName());
        edtCuota.setText(String.valueOf(cvCuota));


        btnGuardar.setOnClickListener(V -> {
            String name = edtName.getText().toString();
            String entidad = edtEntidad.getText().toString();
            String valor = edtValor.getText().toString();
            String plazo = edtPlazo.getText().toString();
            String tasa = edtTasa.getText().toString();
            String cuota = edtCuota.getText().toString();

            int valorInt = Integer.parseInt(valor.replace(",", "").replace(".", ""));
            int plazoInt = Integer.parseInt(plazo);
            double tasaDou = Double.valueOf(tasa);
            int cuotaInt = Integer.parseInt(cuota.replace(",", "").replace(".", ""));

            boolean validate = true;

            if (name.isEmpty()) {
                edtName.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (tasa.isEmpty()) {
                edtTasa.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (entidad.isEmpty()) {
                edtEntidad.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (valor.isEmpty()) {
                edtValor.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (spinner.getSelectedItemPosition() == 0) {
                validate = false;
            }
            if (plazo.isEmpty() && spinner.getSelectedItemPosition() == 2) {
                edtPlazo.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (cuota.isEmpty() && spinner.getSelectedItemPosition() == 1) {
                edtCuota.setError(getString(R.string.str_error_field));
                validate = false;
            }


            if (validate && spinner.getSelectedItemPosition() == 2) {
                CreditEntity creditEntity = new CreditEntity(name, valorInt, plazoInt, entidad, tasaDou);
                List<DetailCreditEntity> listDetail = getDetailFromPlazo(creditEntity.getValue(), creditEntity.getTasa(), creditEntity.getDuration());
                mViewModel.deleteCredit(entity.getId());
                mViewModel.insertCredit(creditEntity, listDetail);
                this.dismiss();
            } else if (validate && spinner.getSelectedItemPosition() == 1) {

                List<DetailCreditEntity> listDetail = getDetailFromCuota(valorInt, tasaDou, cuotaInt);
                double plazoD = getPlazo(valorInt, tasaDou / 100, cuotaInt);
                int plazoCalc = 0;
                if (plazoD - (int) plazoD > 0) {
                    plazoCalc = (int) plazoD + 1;
                }
                CreditEntity creditEntity = new CreditEntity(name, valorInt, plazoCalc, entidad, tasaDou);
                mViewModel.deleteCredit(entity.getId());
                mViewModel.insertCredit(creditEntity, listDetail);
                this.dismiss();

            }
        });


        btnCancelar.setOnClickListener(v -> this.dismiss());
        builder.setView(view);

        return builder.create();

    }
}
