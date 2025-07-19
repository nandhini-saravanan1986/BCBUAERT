package com.bornfire.xbrl.services;

import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_RepoDataTemplate;
import com.bornfire.xbrl.entities.RT_RepoDataTemplateRepository;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
public class RT_RepoService {

	 @Autowired
	 RT_RepoDataTemplateRepository repoRepo;

	 @Autowired
	private Environment env;
	 
	    public boolean updateRepoData(RT_RepoDataTemplate newRepoData) {
	        Optional<RT_RepoDataTemplate> existingDataOpt = repoRepo.findById(newRepoData.getSlNo());

	        if (existingDataOpt.isPresent()) {
	            RT_RepoDataTemplate existingData = existingDataOpt.get();

	            existingData.setDataDate(newRepoData.getDataDate());
	            existingData.setBankName(newRepoData.getBankName());
	            existingData.setHeadOfficeSubsidiary(newRepoData.getHeadOfficeSubsidiary());
	            existingData.setSubsidiary(newRepoData.getSubsidiary());
	            existingData.setBankSymbol(newRepoData.getBankSymbol());
	            existingData.setConventionalIslamic(newRepoData.getConventionalIslamic());
	            existingData.setLocalForeign(newRepoData.getLocalForeign());
	            existingData.setCbuaeTiering(newRepoData.getCbuaeTiering());
	            existingData.setCounterpartyName(newRepoData.getCounterpartyName());
	            existingData.setCounterpartyInternalRef(newRepoData.getCounterpartyInternalRef());
	            existingData.setFinalRatingBanks(newRepoData.getFinalRatingBanks());
	            existingData.setFinalRatingCbuae(newRepoData.getFinalRatingCbuae());
	            existingData.setCountryOfRisk(newRepoData.getCountryOfRisk());
	            existingData.setCbuaeGeoZone(newRepoData.getCbuaeGeoZone());
	            existingData.setCbuaeInternalRef(newRepoData.getCbuaeInternalRef());
	            existingData.setTransactionType(newRepoData.getTransactionType());
	            existingData.setDealNo(newRepoData.getDealNo());
	            existingData.setRepoStartDate(newRepoData.getRepoStartDate());
	            existingData.setRepoMaturityDate(newRepoData.getRepoMaturityDate());
	            existingData.setInitialMaturity(newRepoData.getInitialMaturity());
	            existingData.setInitialMaturityRounded(newRepoData.getInitialMaturityRounded());
	            existingData.setInitialMaturityPeriod(newRepoData.getInitialMaturityPeriod());
	            existingData.setResidualMaturityDate(newRepoData.getResidualMaturityDate());
	            existingData.setMaturityPeriod(newRepoData.getMaturityPeriod());
	            existingData.setRepoCurrency(newRepoData.getRepoCurrency());
	            existingData.setUnderlyingSecurityIsin(newRepoData.getUnderlyingSecurityIsin());
	            existingData.setRehypothecationStatus(newRepoData.getRehypothecationStatus());
	            existingData.setSecurityCurrency(newRepoData.getSecurityCurrency());
	            existingData.setObligor(newRepoData.getObligor());
	            existingData.setIssuerType(newRepoData.getIssuerType());
	            existingData.setIndustry(newRepoData.getIndustry());
	            existingData.setSector(newRepoData.getSector());
	            existingData.setUnderlyingBondDetails(newRepoData.getUnderlyingBondDetails());
	            existingData.setSecurityFinalRating(newRepoData.getSecurityFinalRating());
	            existingData.setFinalSecurityRatingCbuae(newRepoData.getFinalSecurityRatingCbuae());
	            existingData.setSecurityCountryOfRisk(newRepoData.getSecurityCountryOfRisk());
	            existingData.setCbuaeGeoZone2(newRepoData.getCbuaeGeoZone2());
	            existingData.setBondSukukNominal(newRepoData.getBondSukukNominal());
	            existingData.setSecurityMaturityDate(newRepoData.getSecurityMaturityDate());
	            existingData.setSecurityResidualMaturity(newRepoData.getSecurityResidualMaturity());
	            existingData.setSecurityMaturityPeriod(newRepoData.getSecurityMaturityPeriod());
	            existingData.setGmraEffectiveHaircut(newRepoData.getGmraEffectiveHaircut());
	            existingData.setSecurityStartPrice(newRepoData.getSecurityStartPrice());
	            existingData.setSecurityStartCashAmount(newRepoData.getSecurityStartCashAmount());
	            existingData.setStartCashAmount(newRepoData.getStartCashAmount());
	            existingData.setInterestProfitRate(newRepoData.getInterestProfitRate());
	            existingData.setFixedRate(newRepoData.getFixedRate());
	            existingData.setFloatingRateType(newRepoData.getFloatingRateType());
	            existingData.setRepoMargin(newRepoData.getRepoMargin());
	            existingData.setInterestAccruals(newRepoData.getInterestAccruals());
	            existingData.setRepoValuation(newRepoData.getRepoValuation());
	            existingData.setDirtyPrice(newRepoData.getDirtyPrice());
	            existingData.setGmraCollateralMtm(newRepoData.getGmraCollateralMtm());
	            existingData.setMinTransferAmount(newRepoData.getMinTransferAmount());
	            existingData.setThreshold(newRepoData.getThreshold());
	            existingData.setMarginCallFreq(newRepoData.getMarginCallFreq());


	            repoRepo.save(existingData);
	            return true;
	        }

	        return false;
	    }
	    
	    public File generateRepoExcel() {
	        File outputFile = null;

	        try {
	            List<Object[]> repoList = repoRepo.getRepoDataList();
	            File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_Repo_Data_Template.xls");
	            Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
	            Sheet sheet = workbook.getSheetAt(0);

	            CreationHelper createHelper = workbook.getCreationHelper();
	            short dateFormat = createHelper.createDataFormat().getFormat("dd-MM-yyyy");

	            int startRow = 3;

	            if (!repoList.isEmpty()) {
	                for (int i = 0; i < repoList.size(); i++) {
	                    Object[] data = repoList.get(i);
	                    Row row = sheet.getRow(startRow + i);
	                    if (row == null)
	                        row = sheet.createRow(startRow + i);

	                    for (int j = 0; j < data.length; j++) {
	                        Cell cell = row.getCell(j);
	                        if (cell == null)
	                            cell = row.createCell(j);

	                        Object value = data[j];

	                        // Preserve the original style from the cell
	                        CellStyle originalStyle = cell.getCellStyle();

	                        if (value instanceof Date) {
	                            cell.setCellValue((Date) value);
	                            // Clone original style and apply only date format
	                            CellStyle dateCellStyle = workbook.createCellStyle();
	                            dateCellStyle.cloneStyleFrom(originalStyle);
	                            dateCellStyle.setDataFormat(dateFormat);
	                            cell.setCellStyle(dateCellStyle);
	                        } else if (value instanceof BigDecimal) {
	                            cell.setCellValue(((BigDecimal) value).doubleValue());
	                            cell.setCellStyle(originalStyle);
	                        } else if (value instanceof Number) {
	                            cell.setCellValue(((Number) value).doubleValue());
	                            cell.setCellStyle(originalStyle);
	                        } else {
	                            cell.setCellValue(value != null ? value.toString() : "");
	                            cell.setCellStyle(originalStyle);
	                        }
	                    }
	                }

	                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

	                outputFile = new File(env.getProperty("output.exportpathfinal") + "CBUAE_Repo_Data_Template.xls");
	                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
	                    workbook.write(fos);
	                    System.out.println("Repo Excel generated: " + outputFile.getAbsolutePath());
	                }

	                workbook.close();
	            } else {
	                System.out.println("No Repo data found to export.");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return outputFile;
	    }


	    
}
