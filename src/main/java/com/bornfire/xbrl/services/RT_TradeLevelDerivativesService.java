package com.bornfire.xbrl.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivatives;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesRepository;

@Service
public class RT_TradeLevelDerivativesService {

    @Autowired
    private Environment env;

    @Autowired
    private RT_TradeLevelDataDerivativesRepository tradeleveldataderivaticeRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updatetradeleveldataderivative(RT_TradeLevelDataDerivatives updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_TradeLevelDataDerivatives existing = tradeleveldataderivaticeRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	       
	    	existing.setBank_name(updatedData.getBank_name());
	    	existing.setHead_office_subsidiary(updatedData.getHead_office_subsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBank_symbol(updatedData.getBank_symbol());
	    	


	    	tradeleveldataderivaticeRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    
    public File generateTradeleveldataderivativeExcel() {
        File outputFile = null;

        try {
            // Fetch data from repository
            List<Object[]> tradeleveldataDerivative = tradeleveldataderivaticeRepo.gettradeleveldataderivative1();

            // Load Excel template file
            File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_Trade_Level_Data_Derivative_Template.xls");
            Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
            Sheet sheet = workbook.getSheetAt(0);

            // Create date cell style
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int startRow = 4; // Assuming data starts from row index 3 (4rd row)

            if (!tradeleveldataDerivative.isEmpty()) {
                for (int i = 0; i < tradeleveldataDerivative.size(); i++) {
                    Object[] mm = tradeleveldataDerivative.get(i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) row = sheet.createRow(startRow + i);

                 // 0 - bank_date (DATE)
                    Cell cell0 = row.getCell(0);
                    if (cell0 == null) cell0 = row.createCell(0);
                    if (mm[0] instanceof Date) {
                        cell0.setCellValue((Date) mm[0]);
                        cell0.setCellStyle(dateStyle);
                    } else {
                        cell0.setCellValue("");
                    }

                    // 1 - bank_name (VARCHAR2)
                    Cell cell1 = row.getCell(1);
                    if (cell1 == null) cell1 = row.createCell(1);
                    cell1.setCellValue(mm[1] == null ? "" : mm[1].toString());

                    // 2 - head_office_subsidiary
                    Cell cell2 = row.getCell(2);
                    if (cell2 == null) cell2 = row.createCell(2);
                    cell2.setCellValue(mm[2] == null ? "" : mm[2].toString());

                    // 3 - subsidiary
                    Cell cell3 = row.getCell(3);
                    if (cell3 == null) cell3 = row.createCell(3);
                    cell3.setCellValue(mm[3] == null ? "" : mm[3].toString());

                    // 4 - bank_symbol
                    Cell cell4 = row.getCell(4);
                    if (cell4 == null) cell4 = row.createCell(4);
                    cell4.setCellValue(mm[4] == null ? "" : mm[4].toString());

                    // 5 - conventional_islamic
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) cell5 = row.createCell(5);
                    cell5.setCellValue(mm[5] == null ? "" : mm[5].toString());

                    // 6 - cbuae_tiering1
                    Cell cell6 = row.getCell(6);
                    if (cell6 == null) cell6 = row.createCell(6);
                    cell6.setCellValue(mm[6] == null ? "" : mm[6].toString());

                    // 7 - cbuae_tiering2
                    Cell cell7 = row.getCell(7);
                    if (cell7 == null) cell7 = row.createCell(7);
                    cell7.setCellValue(mm[7] == null ? "" : mm[7].toString());

                    // 8 - cpty_name
                    Cell cell8 = row.getCell(8);
                    if (cell8 == null) cell8 = row.createCell(8);
                    cell8.setCellValue(mm[8] == null ? "" : mm[8].toString());

                    // 9 - internal_counterparty_ref
                    Cell cell9 = row.getCell(9);
                    if (cell9 == null) cell9 = row.createCell(9);
                    cell9.setCellValue(mm[9] == null ? "" : mm[9].toString());

                    // 10 - internal_transaction_ref
                    Cell cell10 = row.getCell(10);
                    if (cell10 == null) cell10 = row.createCell(10);
                    cell10.setCellValue(mm[10] == null ? "" : mm[10].toString());

                    // 11 - underlying_asset_class
                    Cell cell11 = row.getCell(11);
                    if (cell11 == null) cell11 = row.createCell(11);
                    cell11.setCellValue(mm[11] == null ? "" : mm[11].toString());

                    // 12 - derivatives_type
                    Cell cell12 = row.getCell(12);
                    if (cell12 == null) cell12 = row.createCell(12);
                    cell12.setCellValue(mm[12] == null ? "" : mm[12].toString());

                    // 13 - transaction_rationale
                    Cell cell13 = row.getCell(13);
                    if (cell13 == null) cell13 = row.createCell(13);
                    cell13.setCellValue(mm[13] == null ? "" : mm[13].toString());

                    // 14 - bundle_transaction
                    Cell cell14 = row.getCell(14);
                    if (cell14 == null) cell14 = row.createCell(14);
                    cell14.setCellValue(mm[14] == null ? "" : mm[14].toString());

                    // 15 - back_to_back_transaction
                    Cell cell15 = row.getCell(15);
                    if (cell15 == null) cell15 = row.createCell(15);
                    cell15.setCellValue(mm[15] == null ? "" : mm[15].toString());

                    // 16 - instruments_characteristics
                    Cell cell16 = row.getCell(16);
                    if (cell16 == null) cell16 = row.createCell(16);
                    cell16.setCellValue(mm[16] == null ? "" : mm[16].toString());

                    // 17 - currency_valuation
                    Cell cell17 = row.getCell(17);
                    if (cell17 == null) cell17 = row.createCell(17);
                    cell17.setCellValue(mm[17] == null ? "" : mm[17].toString());

                    // 18 - aed_fx_spot_rate_conversion
                    Cell cell18 = row.getCell(18);
                    if (cell18 == null) cell18 = row.createCell(18);
                    cell18.setCellValue(mm[18] == null ? "" : mm[18].toString());

                    // 19 - start_date (DATE)
                    Cell cell19 = row.getCell(19);
                    if (cell19 == null) cell19 = row.createCell(19);
                    if (mm[19] instanceof Date) {
                        cell19.setCellValue((Date) mm[19]);
                        cell19.setCellStyle(dateStyle);
                    } else {
                        cell19.setCellValue("");
                    }

                    // 20 - effective_delivery_date (DATE)
                    Cell cell20 = row.getCell(20);
                    if (cell20 == null) cell20 = row.createCell(20);
                    if (mm[20] instanceof Date) {
                        cell20.setCellValue((Date) mm[20]);
                        cell20.setCellStyle(dateStyle);
                    } else {
                        cell20.setCellValue("");
                    }

                    // 21 - final_maturity_date (DATE)
                    Cell cell21 = row.getCell(21);
                    if (cell21 == null) cell21 = row.createCell(21);
                    if (mm[21] instanceof Date) {
                        cell21.setCellValue((Date) mm[21]);
                        cell21.setCellStyle(dateStyle);
                    } else {
                        cell21.setCellValue("");
                    }

                    // 22 - residual_maturity
                    Cell cell22 = row.getCell(22);
                    if (cell22 == null) cell22 = row.createCell(22);
                    cell22.setCellValue(mm[22] == null ? "" : mm[22].toString());

                    // 23 - residual_maturity_rounded
                    Cell cell23 = row.getCell(23);
                    if (cell23 == null) cell23 = row.createCell(23);
                    cell23.setCellValue(mm[23] == null ? "" : mm[23].toString());

                    // 24 - maturity_period
                    Cell cell24 = row.getCell(24);
                    if (cell24 == null) cell24 = row.createCell(24);
                    cell24.setCellValue(mm[24] == null ? "" : mm[24].toString());

                    // 25 - position
                    Cell cell25 = row.getCell(25);
                    if (cell25 == null) cell25 = row.createCell(25);
                    cell25.setCellValue(mm[25] == null ? "" : mm[25].toString());

                    // 26 - notional_type
                    Cell cell26 = row.getCell(26);
                    if (cell26 == null) cell26 = row.createCell(26);
                    cell26.setCellValue(mm[26] == null ? "" : mm[26].toString());

                    // 27 - cancellable
                    Cell cell27 = row.getCell(27);
                    if (cell27 == null) cell27 = row.createCell(27);
                    cell27.setCellValue(mm[27] == null ? "" : mm[27].toString());

                    // 28 - cancellable_date (DATE)
                    Cell cell28 = row.getCell(28);
                    if (cell28 == null) cell28 = row.createCell(28);
                    if (mm[28] instanceof Date) {
                        cell28.setCellValue((Date) mm[28]);
                        cell28.setCellStyle(dateStyle);
                    } else {
                        cell28.setCellValue("");
                    }
                    
                 // 29 - cancellable_option_type
                    Cell cell29 = row.getCell(29);
                    if (cell29 == null) cell29 = row.createCell(29);
                    cell29.setCellValue(mm[29] == null ? "" : mm[29].toString());

                    // 30 - notional1_assets (BigDecimal)
                    Cell cell30 = row.getCell(30);
                    if (cell30 == null) cell30 = row.createCell(30);
                    if (mm[30] instanceof BigDecimal) {
                        cell30.setCellValue(((BigDecimal) mm[30]).doubleValue());
                    } else {
                        cell30.setCellValue(0);
                    }

                    // 31 - notional1_currency
                    Cell cell31 = row.getCell(31);
                    if (cell31 == null) cell31 = row.createCell(31);
                    cell31.setCellValue(mm[31] == null ? "" : mm[31].toString());

                    // 32 - pay_receive1
                    Cell cell32 = row.getCell(32);
                    if (cell32 == null) cell32 = row.createCell(32);
                    cell32.setCellValue(mm[32] == null ? "" : mm[32].toString());

                    // 33 - coupon_fixed_rate1 (BigDecimal)
                    Cell cell33 = row.getCell(33);
                    if (cell33 == null) cell33 = row.createCell(33);
                    if (mm[33] instanceof BigDecimal) {
                        cell33.setCellValue(((BigDecimal) mm[33]).doubleValue());
                    } else {
                        cell33.setCellValue(0);
                    }

                    // 34 - margin1 (BigDecimal)
                    Cell cell34 = row.getCell(34);
                    if (cell34 == null) cell34 = row.createCell(34);
                    if (mm[34] instanceof BigDecimal) {
                        cell34.setCellValue(((BigDecimal) mm[34]).doubleValue());
                    } else {
                        cell34.setCellValue(0);
                    }

                    // 35 - benchmark_floating_rate1
                    Cell cell35 = row.getCell(35);
                    if (cell35 == null) cell35 = row.createCell(35);
                    cell35.setCellValue(mm[35] == null ? "" : mm[35].toString());

                    // 36 - benchmark_rate_tenor1
                    Cell cell36 = row.getCell(36);
                    if (cell36 == null) cell36 = row.createCell(36);
                    cell36.setCellValue(mm[36] == null ? "" : mm[36].toString());

                    // 37 - notional2_liabilities (BigDecimal)
                    Cell cell37 = row.getCell(37);
                    if (cell37 == null) cell37 = row.createCell(37);
                    if (mm[37] instanceof BigDecimal) {
                        cell37.setCellValue(((BigDecimal) mm[37]).doubleValue());
                    } else {
                        cell37.setCellValue(0);
                    }

                    // 38 - notional2_currency
                    Cell cell38 = row.getCell(38);
                    if (cell38 == null) cell38 = row.createCell(38);
                    cell38.setCellValue(mm[38] == null ? "" : mm[38].toString());

                    // 39 - pay_receive2
                    Cell cell39 = row.getCell(39);
                    if (cell39 == null) cell39 = row.createCell(39);
                    cell39.setCellValue(mm[39] == null ? "" : mm[39].toString());

                    // 40 - coupon_fixed_rate2 (BigDecimal)
                    Cell cell40 = row.getCell(40);
                    if (cell40 == null) cell40 = row.createCell(40);
                    if (mm[40] instanceof BigDecimal) {
                        cell40.setCellValue(((BigDecimal) mm[40]).doubleValue());
                    } else {
                        cell40.setCellValue(0);
                    }

                    // 41 - margin2 (BigDecimal)
                    Cell cell41 = row.getCell(41);
                    if (cell41 == null) cell41 = row.createCell(41);
                    if (mm[41] instanceof BigDecimal) {
                        cell41.setCellValue(((BigDecimal) mm[41]).doubleValue());
                    } else {
                        cell41.setCellValue(0);
                    }

                    // 42 - benchmark_floating_rate2
                    Cell cell42 = row.getCell(42);
                    if (cell42 == null) cell42 = row.createCell(42);
                    cell42.setCellValue(mm[42] == null ? "" : mm[42].toString());

                    // 43 - benchmark_rate_tenor2
                    Cell cell43 = row.getCell(43);
                    if (cell43 == null) cell43 = row.createCell(43);
                    cell43.setCellValue(mm[43] == null ? "" : mm[43].toString());
                    
                 // 44 - clean_value_aed (BigDecimal)
                    Cell cell44 = row.getCell(44);
                    if (cell44 == null) cell44 = row.createCell(44);
                    if (mm[44] instanceof BigDecimal) {
                        cell44.setCellValue(((BigDecimal) mm[44]).doubleValue());
                    } else {
                        cell44.setCellValue(0);
                    }

                    // 45 - accrued_interest_aed (BigDecimal)
                    Cell cell45 = row.getCell(45);
                    if (cell45 == null) cell45 = row.createCell(45);
                    if (mm[45] instanceof BigDecimal) {
                        cell45.setCellValue(((BigDecimal) mm[45]).doubleValue());
                    } else {
                        cell45.setCellValue(0);
                    }

                    // 46 - dv01_aed (BigDecimal)
                    Cell cell46 = row.getCell(46);
                    if (cell46 == null) cell46 = row.createCell(46);
                    if (mm[46] instanceof BigDecimal) {
                        cell46.setCellValue(((BigDecimal) mm[46]).doubleValue());
                    } else {
                        cell46.setCellValue(0);
                    }

                    // 47 - br01_aed (BigDecimal)
                    Cell cell47 = row.getCell(47);
                    if (cell47 == null) cell47 = row.createCell(47);
                    if (mm[47] instanceof BigDecimal) {
                        cell47.setCellValue(((BigDecimal) mm[47]).doubleValue());
                    } else {
                        cell47.setCellValue(0);
                    }

                    // 48 - dollar_delta_aed (BigDecimal)
                    Cell cell48 = row.getCell(48);
                    if (cell48 == null) cell48 = row.createCell(48);
                    if (mm[48] instanceof BigDecimal) {
                        cell48.setCellValue(((BigDecimal) mm[48]).doubleValue());
                    } else {
                        cell48.setCellValue(0);
                    }

                    // 49 - dollar_vega_aed (BigDecimal)
                    Cell cell49 = row.getCell(49);
                    if (cell49 == null) cell49 = row.createCell(49);
                    if (mm[49] instanceof BigDecimal) {
                        cell49.setCellValue(((BigDecimal) mm[49]).doubleValue());
                    } else {
                        cell49.setCellValue(0);
                    }

                    // 50 - dollar_gamma_aed (BigDecimal)
                    Cell cell50 = row.getCell(50);
                    if (cell50 == null) cell50 = row.createCell(50);
                    if (mm[50] instanceof BigDecimal) {
                        cell50.setCellValue(((BigDecimal) mm[50]).doubleValue());
                    } else {
                        cell50.setCellValue(0);
                    }

                    // 51 - dollar_theta_aed (BigDecimal)
                    Cell cell51 = row.getCell(51);
                    if (cell51 == null) cell51 = row.createCell(51);
                    if (mm[51] instanceof BigDecimal) {
                        cell51.setCellValue(((BigDecimal) mm[51]).doubleValue());
                    } else {
                        cell51.setCellValue(0);
                    }

                    // 52 - uleg1_notional (BigDecimal)
                    Cell cell52 = row.getCell(52);
                    if (cell52 == null) cell52 = row.createCell(52);
                    if (mm[52] instanceof BigDecimal) {
                        cell52.setCellValue(((BigDecimal) mm[52]).doubleValue());
                    } else {
                        cell52.setCellValue(0);
                    }

                    // 53 - uleg1_currency (String)
                    Cell cell53 = row.getCell(53);
                    if (cell53 == null) cell53 = row.createCell(53);
                    cell53.setCellValue(mm[53] == null ? "" : mm[53].toString());

                    // 54 - uleg1_pay_receive (String)
                    Cell cell54 = row.getCell(54);
                    if (cell54 == null) cell54 = row.createCell(54);
                    cell54.setCellValue(mm[54] == null ? "" : mm[54].toString());

                    // 55 - uleg1_coupon (BigDecimal)
                    Cell cell55 = row.getCell(55);
                    if (cell55 == null) cell55 = row.createCell(55);
                    if (mm[55] instanceof BigDecimal) {
                        cell55.setCellValue(((BigDecimal) mm[55]).doubleValue());
                    } else {
                        cell55.setCellValue(0);
                    }

                    // 56 - uleg1_margin (BigDecimal)
                    Cell cell56 = row.getCell(56);
                    if (cell56 == null) cell56 = row.createCell(56);
                    if (mm[56] instanceof BigDecimal) {
                        cell56.setCellValue(((BigDecimal) mm[56]).doubleValue());
                    } else {
                        cell56.setCellValue(0);
                    }

                    // 57 - uleg1_benchmark (String)
                    Cell cell57 = row.getCell(57);
                    if (cell57 == null) cell57 = row.createCell(57);
                    cell57.setCellValue(mm[57] == null ? "" : mm[57].toString());

                    // 58 - uleg1_tenor (String)
                    Cell cell58 = row.getCell(58);
                    if (cell58 == null) cell58 = row.createCell(58);
                    cell58.setCellValue(mm[58] == null ? "" : mm[58].toString());

                    // 59 - uleg1_details (String)
                    Cell cell59 = row.getCell(59);
                    if (cell59 == null) cell59 = row.createCell(59);
                    cell59.setCellValue(mm[59] == null ? "" : mm[59].toString());

                    // 60 - uleg2_notional (BigDecimal)
                    Cell cell60 = row.getCell(60);
                    if (cell60 == null) cell60 = row.createCell(60);
                    if (mm[60] instanceof BigDecimal) {
                        cell60.setCellValue(((BigDecimal) mm[60]).doubleValue());
                    } else {
                        cell60.setCellValue(0);
                    }

                    // 61 - uleg2_currency (String)
                    Cell cell61 = row.getCell(61);
                    if (cell61 == null) cell61 = row.createCell(61);
                    cell61.setCellValue(mm[61] == null ? "" : mm[61].toString());

                    // 62 - uleg2_pay_receive (String)
                    Cell cell62 = row.getCell(62);
                    if (cell62 == null) cell62 = row.createCell(62);
                    cell62.setCellValue(mm[62] == null ? "" : mm[62].toString());

                    // 63 - uleg2_coupon (BigDecimal)
                    Cell cell63 = row.getCell(63);
                    if (cell63 == null) cell63 = row.createCell(63);
                    if (mm[63] instanceof BigDecimal) {
                        cell63.setCellValue(((BigDecimal) mm[63]).doubleValue());
                    } else {
                        cell63.setCellValue(0);
                    }

                    // 64 - uleg2_margin (BigDecimal)
                    Cell cell64 = row.getCell(64);
                    if (cell64 == null) cell64 = row.createCell(64);
                    if (mm[64] instanceof BigDecimal) {
                        cell64.setCellValue(((BigDecimal) mm[64]).doubleValue());
                    } else {
                        cell64.setCellValue(0);
                    }

                    // 65 - uleg2_benchmark (String)
                    Cell cell65 = row.getCell(65);
                    if (cell65 == null) cell65 = row.createCell(65);
                    cell65.setCellValue(mm[65] == null ? "" : mm[65].toString());

                    // 66 - uleg2_tenor (String)
                    Cell cell66 = row.getCell(66);
                    if (cell66 == null) cell66 = row.createCell(66);
                    cell66.setCellValue(mm[66] == null ? "" : mm[66].toString());

                    // 67 - uleg2_details (String)
                    Cell cell67 = row.getCell(67);
                    if (cell67 == null) cell67 = row.createCell(67);
                    cell67.setCellValue(mm[67] == null ? "" : mm[67].toString());

                    // 68 - uleg2_clean_value_aed (BigDecimal)
                    Cell cell68 = row.getCell(68);
                    if (cell68 == null) cell68 = row.createCell(68);
                    if (mm[68] instanceof BigDecimal) {
                        cell68.setCellValue(((BigDecimal) mm[68]).doubleValue());
                    } else {
                        cell68.setCellValue(0);
                    }

                    // 69 - uleg2_accrued_interest_aed (BigDecimal)
                    Cell cell69 = row.getCell(69);
                    if (cell69 == null) cell69 = row.createCell(69);
                    if (mm[69] instanceof BigDecimal) {
                        cell69.setCellValue(((BigDecimal) mm[69]).doubleValue());
                    } else {
                        cell69.setCellValue(0);
                    }

                    // 70 - uleg2_dv01_aed (BigDecimal)
                    Cell cell70 = row.getCell(70);
                    if (cell70 == null) cell70 = row.createCell(70);
                    if (mm[70] instanceof BigDecimal) {
                        cell70.setCellValue(((BigDecimal) mm[70]).doubleValue());
                    } else {
                        cell70.setCellValue(0);
                    }

                    // 71 - cds_type (String)
                    Cell cell71 = row.getCell(71);
                    if (cell71 == null) cell71 = row.createCell(71);
                    cell71.setCellValue(mm[71] == null ? "" : mm[71].toString());

                    // 72 - cds_underlyings (String)
                    Cell cell72 = row.getCell(72);
                    if (cell72 == null) cell72 = row.createCell(72);
                    cell72.setCellValue(mm[72] == null ? "" : mm[72].toString());

                    // 73 - quanto (String)
                    Cell cell73 = row.getCell(73);
                    if (cell73 == null) cell73 = row.createCell(73);
                    cell73.setCellValue(mm[73] == null ? "" : mm[73].toString());
                    
                 // 74 - nominal (BigDecimal)
                    Cell cell74 = row.getCell(74);
                    if (cell74 == null) cell74 = row.createCell(74);
                    if (mm[74] instanceof BigDecimal) {
                        cell74.setCellValue(((BigDecimal) mm[74]).doubleValue());
                    } else {
                        cell74.setCellValue(0);
                    }

                    // 75 - ir_dv01_aed (BigDecimal)
                    Cell cell75 = row.getCell(75);
                    if (cell75 == null) cell75 = row.createCell(75);
                    if (mm[75] instanceof BigDecimal) {
                        cell75.setCellValue(((BigDecimal) mm[75]).doubleValue());
                    } else {
                        cell75.setCellValue(0);
                    }

                    // 76 - cs01_aed (BigDecimal)
                    Cell cell76 = row.getCell(76);
                    if (cell76 == null) cell76 = row.createCell(76);
                    if (mm[76] instanceof BigDecimal) {
                        cell76.setCellValue(((BigDecimal) mm[76]).doubleValue());
                    } else {
                        cell76.setCellValue(0);
                    }

                    // 77 - future_type (String)
                    Cell cell77 = row.getCell(77);
                    if (cell77 == null) cell77 = row.createCell(77);
                    cell77.setCellValue(mm[77] == null ? "" : mm[77].toString());

                    // 78 - future_underlying_contract (String)
                    Cell cell78 = row.getCell(78);
                    if (cell78 == null) cell78 = row.createCell(78);
                    cell78.setCellValue(mm[78] == null ? "" : mm[78].toString());

                    // 79 - future_qty (BigDecimal)
                    Cell cell79 = row.getCell(79);
                    if (cell79 == null) cell79 = row.createCell(79);
                    if (mm[79] instanceof BigDecimal) {
                        cell79.setCellValue(((BigDecimal) mm[79]).doubleValue());
                    } else {
                        cell79.setCellValue(0);
                    }

                    // 80 - future_contract_size (BigDecimal)
                    Cell cell80 = row.getCell(80);
                    if (cell80 == null) cell80 = row.createCell(80);
                    if (mm[80] instanceof BigDecimal) {
                        cell80.setCellValue(((BigDecimal) mm[80]).doubleValue());
                    } else {
                        cell80.setCellValue(0);
                    }

                    // 81 - future_contract_rate (BigDecimal)
                    Cell cell81 = row.getCell(81);
                    if (cell81 == null) cell81 = row.createCell(81);
                    if (mm[81] instanceof BigDecimal) {
                        cell81.setCellValue(((BigDecimal) mm[81]).doubleValue());
                    } else {
                        cell81.setCellValue(0);
                    }

                    // 82 - future_market_rate (BigDecimal)
                    Cell cell82 = row.getCell(82);
                    if (cell82 == null) cell82 = row.createCell(82);
                    if (mm[82] instanceof BigDecimal) {
                        cell82.setCellValue(((BigDecimal) mm[82]).doubleValue());
                    } else {
                        cell82.setCellValue(0);
                    }

                    // 83 - future_contract_valuation (BigDecimal)
                    Cell cell83 = row.getCell(83);
                    if (cell83 == null) cell83 = row.createCell(83);
                    if (mm[83] instanceof BigDecimal) {
                        cell83.setCellValue(((BigDecimal) mm[83]).doubleValue());
                    } else {
                        cell83.setCellValue(0);
                    }

                    // 84 - future_sensitivity_aed (BigDecimal)
                    Cell cell84 = row.getCell(84);
                    if (cell84 == null) cell84 = row.createCell(84);
                    if (mm[84] instanceof BigDecimal) {
                        cell84.setCellValue(((BigDecimal) mm[84]).doubleValue());
                    } else {
                        cell84.setCellValue(0);
                    }

                    // 85 - options_list (String)
                    Cell cell85 = row.getCell(85);
                    if (cell85 == null) cell85 = row.createCell(85);
                    cell85.setCellValue(mm[85] == null ? "" : mm[85].toString());

                    // 86 - option_type (String)
                    Cell cell86 = row.getCell(86);
                    if (cell86 == null) cell86 = row.createCell(86);
                    cell86.setCellValue(mm[86] == null ? "" : mm[86].toString());

                    // 87 - option_underlying_type (String)
                    Cell cell87 = row.getCell(87);
                    if (cell87 == null) cell87 = row.createCell(87);
                    cell87.setCellValue(mm[87] == null ? "" : mm[87].toString());

                    // 88 - option_underlying_details (String)
                    Cell cell88 = row.getCell(88);
                    if (cell88 == null) cell88 = row.createCell(88);
                    cell88.setCellValue(mm[88] == null ? "" : mm[88].toString());

                    // 89 - strike (BigDecimal)
                    Cell cell89 = row.getCell(89);
                    if (cell89 == null) cell89 = row.createCell(89);
                    if (mm[89] instanceof BigDecimal) {
                        cell89.setCellValue(((BigDecimal) mm[89]).doubleValue());
                    } else {
                        cell89.setCellValue(0);
                    }

                    // 90 - option_market_rate (BigDecimal)
                    Cell cell90 = row.getCell(90);
                    if (cell90 == null) cell90 = row.createCell(90);
                    if (mm[90] instanceof BigDecimal) {
                        cell90.setCellValue(((BigDecimal) mm[90]).doubleValue());
                    } else {
                        cell90.setCellValue(0);
                    }

                    // 91 - barrier_type (String)
                    Cell cell91 = row.getCell(91);
                    if (cell91 == null) cell91 = row.createCell(91);
                    cell91.setCellValue(mm[91] == null ? "" : mm[91].toString());

                    // 92 - barrier_upper_range (BigDecimal)
                    Cell cell92 = row.getCell(92);
                    if (cell92 == null) cell92 = row.createCell(92);
                    if (mm[92] instanceof BigDecimal) {
                        cell92.setCellValue(((BigDecimal) mm[92]).doubleValue());
                    } else {
                        cell92.setCellValue(0);
                    }

                    // 93 - barrier_lower_range (BigDecimal)
                    Cell cell93 = row.getCell(93);
                    if (cell93 == null) cell93 = row.createCell(93);
                    if (mm[93] instanceof BigDecimal) {
                        cell93.setCellValue(((BigDecimal) mm[93]).doubleValue());
                    } else {
                        cell93.setCellValue(0);
                    }

                    // 94 - option_fair_value_aed (BigDecimal)
                    Cell cell94 = row.getCell(94);
                    if (cell94 == null) cell94 = row.createCell(94);
                    if (mm[94] instanceof BigDecimal) {
                        cell94.setCellValue(((BigDecimal) mm[94]).doubleValue());
                    } else {
                        cell94.setCellValue(0);
                    }

                    // 95 - option_dollar_delta (BigDecimal)
                    Cell cell95 = row.getCell(95);
                    if (cell95 == null) cell95 = row.createCell(95);
                    if (mm[95] instanceof BigDecimal) {
                        cell95.setCellValue(((BigDecimal) mm[95]).doubleValue());
                    } else {
                        cell95.setCellValue(0);
                    }

                    // 96 - option_dollar_vega (BigDecimal)
                    Cell cell96 = row.getCell(96);
                    if (cell96 == null) cell96 = row.createCell(96);
                    if (mm[96] instanceof BigDecimal) {
                        cell96.setCellValue(((BigDecimal) mm[96]).doubleValue());
                    } else {
                        cell96.setCellValue(0);
                    }

                    // 97 - option_dollar_gamma (BigDecimal)
                    Cell cell97 = row.getCell(97);
                    if (cell97 == null) cell97 = row.createCell(97);
                    if (mm[97] instanceof BigDecimal) {
                        cell97.setCellValue(((BigDecimal) mm[97]).doubleValue());
                    } else {
                        cell97.setCellValue(0);
                    }

                    // 98 - option_dollar_rho (BigDecimal)
                    Cell cell98 = row.getCell(98);
                    if (cell98 == null) cell98 = row.createCell(98);
                    if (mm[98] instanceof BigDecimal) {
                        cell98.setCellValue(((BigDecimal) mm[98]).doubleValue());
                    } else {
                        cell98.setCellValue(0);
                    }

                    // 99 - option_dollar_theta (BigDecimal)
                    Cell cell99 = row.getCell(99);
                    if (cell99 == null) cell99 = row.createCell(99);
                    if (mm[99] instanceof BigDecimal) {
                        cell99.setCellValue(((BigDecimal) mm[99]).doubleValue());
                    } else {
                        cell99.setCellValue(0);
                    }

                    // 100 - overall_mtm_aed (BigDecimal)
                    Cell cell100 = row.getCell(100);
                    if (cell100 == null) cell100 = row.createCell(100);
                    if (mm[100] instanceof BigDecimal) {
                        cell100.setCellValue(((BigDecimal) mm[100]).doubleValue());
                    } else {
                        cell100.setCellValue(0);
                    }

                    // 101 - irs_positive_mtm (BigDecimal)
                    Cell cell101 = row.getCell(101);
                    if (cell101 == null) cell101 = row.createCell(101);
                    if (mm[101] instanceof BigDecimal) {
                        cell101.setCellValue(((BigDecimal) mm[101]).doubleValue());
                    } else {
                        cell101.setCellValue(0);
                    }

                    // 102 - asset_swap_positive_mtm (BigDecimal)
                    Cell cell102 = row.getCell(102);
                    if (cell102 == null) cell102 = row.createCell(102);
                    if (mm[102] instanceof BigDecimal) {
                        cell102.setCellValue(((BigDecimal) mm[102]).doubleValue());
                    } else {
                        cell102.setCellValue(0);
                    }

                    // 103 - cds_positive_mtm (BigDecimal)
                    Cell cell103 = row.getCell(103);
                    if (cell103 == null) cell103 = row.createCell(103);
                    if (mm[103] instanceof BigDecimal) {
                        cell103.setCellValue(((BigDecimal) mm[103]).doubleValue());
                    } else {
                        cell103.setCellValue(0);
                    }

                    // 104 - future_contract_positive_mtm (BigDecimal)
                    Cell cell104 = row.getCell(104);
                    if (cell104 == null) cell104 = row.createCell(104);
                    if (mm[104] instanceof BigDecimal) {
                        cell104.setCellValue(((BigDecimal) mm[104]).doubleValue());
                    } else {
                        cell104.setCellValue(0);
                    }

                    // 105 - option_positive_mtm (BigDecimal)
                    Cell cell105 = row.getCell(105);
                    if (cell105 == null) cell105 = row.createCell(105);
                    if (mm[105] instanceof BigDecimal) {
                        cell105.setCellValue(((BigDecimal) mm[105]).doubleValue());
                    } else {
                        cell105.setCellValue(0);
                    }

                    // 106 - overall_positive_mtm (BigDecimal)
                    Cell cell106 = row.getCell(106);
                    if (cell106 == null) cell106 = row.createCell(106);
                    if (mm[106] instanceof BigDecimal) {
                        cell106.setCellValue(((BigDecimal) mm[106]).doubleValue());
                    } else {
                        cell106.setCellValue(0);
                    }

                    // 107 - individual_positive_contribution (String)
                    Cell cell107 = row.getCell(107);
                    if (cell107 == null) cell107 = row.createCell(107);
                    cell107.setCellValue(mm[107] == null ? "" : mm[107].toString());

                    // 108 - irs_negative_mtm (BigDecimal)
                    Cell cell108 = row.getCell(108);
                    if (cell108 == null) cell108 = row.createCell(108);
                    if (mm[108] instanceof BigDecimal) {
                        cell108.setCellValue(((BigDecimal) mm[108]).doubleValue());
                    } else {
                        cell108.setCellValue(0);
                    }

                    // 109 - asset_swap_negative_mtm (BigDecimal)
                    Cell cell109 = row.getCell(109);
                    if (cell109 == null) cell109 = row.createCell(109);
                    if (mm[109] instanceof BigDecimal) {
                        cell109.setCellValue(((BigDecimal) mm[109]).doubleValue());
                    } else {
                        cell109.setCellValue(0);
                    }

                    // 110 - cds_negative_mtm (BigDecimal)
                    Cell cell110 = row.getCell(110);
                    if (cell110 == null) cell110 = row.createCell(110);
                    if (mm[110] instanceof BigDecimal) {
                        cell110.setCellValue(((BigDecimal) mm[110]).doubleValue());
                    } else {
                        cell110.setCellValue(0);
                    }

                    // 111 - future_contract_negative_mtm (BigDecimal)
                    Cell cell111 = row.getCell(111);
                    if (cell111 == null) cell111 = row.createCell(111);
                    if (mm[111] instanceof BigDecimal) {
                        cell111.setCellValue(((BigDecimal) mm[111]).doubleValue());
                    } else {
                        cell111.setCellValue(0);
                    }

                    // 112 - option_negative_mtm (BigDecimal)
                    Cell cell112 = row.getCell(112);
                    if (cell112 == null) cell112 = row.createCell(112);
                    if (mm[112] instanceof BigDecimal) {
                        cell112.setCellValue(((BigDecimal) mm[112]).doubleValue());
                    } else {
                        cell112.setCellValue(0);
                    }

                    // 113 - overall_negative_mtm (BigDecimal)
                    Cell cell113 = row.getCell(113);
                    if (cell113 == null) cell113 = row.createCell(113);
                    if (mm[113] instanceof BigDecimal) {
                        cell113.setCellValue(((BigDecimal) mm[113]).doubleValue());
                    } else {
                        cell113.setCellValue(0);
                    }

                    // 114 - individual_negative_contribution (String)
                    Cell cell114 = row.getCell(114);
                    if (cell114 == null) cell114 = row.createCell(114);
                    cell114.setCellValue(mm[114] == null ? "" : mm[114].toString());




                    

                   
                }

                // Evaluate formulas
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

                // Write output to file
                outputFile = new File(env.getProperty("output.exportpathfinal") + "Tradeleveldataderivative.xls");
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    workbook.write(fos);
                    System.out.println("Tradeleveldataderivative Excel generated: " + outputFile.getAbsolutePath());
                }

                workbook.close();
            } else {
                System.out.println("No Tradeleveldataderivative data found.");
                workbook.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
