package com.bornfire.xbrl.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template_Repository;

@Service
public class RT_Liquidity_Risk_Data_Service {

    @Autowired
    RT_Liquidity_Risk_Data_Template_Repository LiquidityRiskDataRepository;

    @Autowired
    private Environment env;

    // Update existing record
    public boolean updateLiquidityRisk(RT_Liquidity_Risk_Data_Template updatedEntity) {
        Optional<RT_Liquidity_Risk_Data_Template> existingOpt = LiquidityRiskDataRepository.findById(updatedEntity.getSlno());

        if (existingOpt.isPresent()) {
            RT_Liquidity_Risk_Data_Template existing = existingOpt.get();

            existing.setDataDate(updatedEntity.getDataDate());
            existing.setBankName(updatedEntity.getBankName());
            existing.setHeadOfficeSubsidiary(updatedEntity.getHeadOfficeSubsidiary());
            existing.setBankSymbol(updatedEntity.getBankSymbol());
            existing.setConventionalIslamic(updatedEntity.getConventionalIslamic());
            existing.setLocalForeign(updatedEntity.getLocalForeign());
            existing.setCbuaeTiering(updatedEntity.getCbuaeTiering());
            existing.setGlLevel1(updatedEntity.getGlLevel1());
            existing.setGlLevel2(updatedEntity.getGlLevel2());
            existing.setGlLevel3(updatedEntity.getGlLevel3());
            existing.setOptionType(updatedEntity.getOptionType());
            existing.setRateType(updatedEntity.getRateType());
            existing.setReferenceRate(updatedEntity.getReferenceRate());
            existing.setInstrumentCurrency(updatedEntity.getInstrumentCurrency());

            existing.setOutstandingBalance(updatedEntity.getOutstandingBalance());
            existing.setOvernight(updatedEntity.getOvernight());
            existing.setOnTo1m(updatedEntity.getOnTo1m());
            existing.setOneMTo3m(updatedEntity.getOneMTo3m());
            existing.setThreeMTo6m(updatedEntity.getThreeMTo6m());
            existing.setSixMTo9m(updatedEntity.getSixMTo9m());
            existing.setNineMTo1y(updatedEntity.getNineMTo1y());
            existing.setOneYTo1_5y(updatedEntity.getOneYTo1_5y());
            existing.setOne5yTo2y(updatedEntity.getOne5yTo2y());
            existing.setTwoYTo3y(updatedEntity.getTwoYTo3y());
            existing.setThreeYTo4y(updatedEntity.getThreeYTo4y());
            existing.setFourYTo5y(updatedEntity.getFourYTo5y());
            existing.setFiveYTo6y(updatedEntity.getFiveYTo6y());
            existing.setSixYTo7y(updatedEntity.getSixYTo7y());
            existing.setSevenYTo8y(updatedEntity.getSevenYTo8y());
            existing.setEightYTo9y(updatedEntity.getEightYTo9y());
            existing.setNineYTo10y(updatedEntity.getNineYTo10y());
            existing.setTenYTo15y(updatedEntity.getTenYTo15y());
            existing.setFifteenYTo20y(updatedEntity.getFifteenYTo20y());
            existing.setTwentyYAbove(updatedEntity.getTwentyYAbove());
            existing.setNonMaturing(updatedEntity.getNonMaturing());
            // Final details
            existing.setReportSubmitDate(updatedEntity.getReportSubmitDate());
            existing.setModifyTime(new Date());// track update time
            
            
            LiquidityRiskDataRepository.save(existing);
            return true;
        } else {
            return false;
        }
    }



}
