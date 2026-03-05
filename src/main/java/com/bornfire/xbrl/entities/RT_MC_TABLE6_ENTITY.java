package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "RT_MC_TABLE6")
public class RT_MC_TABLE6_ENTITY {
	
    public String R7_BANK;
    public String R7_PRODUCT;
    public BigDecimal R7_NO_PRO_SNR;
    public BigDecimal R7_NO_TRA_SNR_SPT;
    public BigDecimal R7_NO_CONS_VC;
    public BigDecimal R7_NO_CON_FRA;
    public BigDecimal R7_NO_CON_MRK_FRD;
    public BigDecimal R7_TOT_CON_PRO_CBUAE;
    public BigDecimal R7_TOT_TRA_CBUAE;
    public String R8_BANK;
    public String R8_PRODUCT;
    public BigDecimal R8_NO_PRO_SNR;
    public BigDecimal R8_NO_TRA_SNR_SPT;
    public BigDecimal R8_NO_CONS_VC;
    public BigDecimal R8_NO_CON_FRA;
    public BigDecimal R8_NO_CON_MRK_FRD;
    public BigDecimal R8_TOT_CON_PRO_CBUAE;
    public BigDecimal R8_TOT_TRA_CBUAE;
    public String R9_BANK;
    public String R9_PRODUCT;
    public BigDecimal R9_NO_PRO_SNR;
    public BigDecimal R9_NO_TRA_SNR_SPT;
    public BigDecimal R9_NO_CONS_VC;
    public BigDecimal R9_NO_CON_FRA;
    public BigDecimal R9_NO_CON_MRK_FRD;
    public BigDecimal R9_TOT_CON_PRO_CBUAE;
    public BigDecimal R9_TOT_TRA_CBUAE;
    public String R10_BANK;
    public String R10_PRODUCT;
    public BigDecimal R10_NO_PRO_SNR;
    public BigDecimal R10_NO_TRA_SNR_SPT;
    public BigDecimal R10_NO_CONS_VC;
    public BigDecimal R10_NO_CON_FRA;
    public BigDecimal R10_NO_CON_MRK_FRD;
    public BigDecimal R10_TOT_CON_PRO_CBUAE;
    public BigDecimal R10_TOT_TRA_CBUAE;
    public String R11_BANK;
    public String R11_PRODUCT;
    public BigDecimal R11_NO_PRO_SNR;
    public BigDecimal R11_NO_TRA_SNR_SPT;
    public BigDecimal R11_NO_CONS_VC;
    public BigDecimal R11_NO_CON_FRA;
    public BigDecimal R11_NO_CON_MRK_FRD;
    public BigDecimal R11_TOT_CON_PRO_CBUAE;
    public BigDecimal R11_TOT_TRA_CBUAE;
    public String R12_BANK;
    public String R12_PRODUCT;
    public BigDecimal R12_NO_PRO_SNR;
    public BigDecimal R12_NO_TRA_SNR_SPT;
    public BigDecimal R12_NO_CONS_VC;
    public BigDecimal R12_NO_CON_FRA;
    public BigDecimal R12_NO_CON_MRK_FRD;
    public BigDecimal R12_TOT_CON_PRO_CBUAE;
    public BigDecimal R12_TOT_TRA_CBUAE;
    public String R13_BANK;
    public String R13_PRODUCT;
    public BigDecimal R13_NO_PRO_SNR;
    public BigDecimal R13_NO_TRA_SNR_SPT;
    public BigDecimal R13_NO_CONS_VC;
    public BigDecimal R13_NO_CON_FRA;
    public BigDecimal R13_NO_CON_MRK_FRD;
    public BigDecimal R13_TOT_CON_PRO_CBUAE;
    public BigDecimal R13_TOT_TRA_CBUAE;
    @Id
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    public Date REPORT_DATE;
    public String BRANCH_CODE;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;
    public String SOLID;
    public String BRANCH_NAME;
    public String MODIFY_USERID;
    public String VERIFY_FLG;
	public String getR7_BANK() {
		return R7_BANK;
	}
	public void setR7_BANK(String r7_BANK) {
		R7_BANK = r7_BANK;
	}
	public String getR7_PRODUCT() {
		return R7_PRODUCT;
	}
	public void setR7_PRODUCT(String r7_PRODUCT) {
		R7_PRODUCT = r7_PRODUCT;
	}
	public BigDecimal getR7_NO_PRO_SNR() {
		return R7_NO_PRO_SNR;
	}
	public void setR7_NO_PRO_SNR(BigDecimal r7_NO_PRO_SNR) {
		R7_NO_PRO_SNR = r7_NO_PRO_SNR;
	}
	public BigDecimal getR7_NO_TRA_SNR_SPT() {
		return R7_NO_TRA_SNR_SPT;
	}
	public void setR7_NO_TRA_SNR_SPT(BigDecimal r7_NO_TRA_SNR_SPT) {
		R7_NO_TRA_SNR_SPT = r7_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR7_NO_CONS_VC() {
		return R7_NO_CONS_VC;
	}
	public void setR7_NO_CONS_VC(BigDecimal r7_NO_CONS_VC) {
		R7_NO_CONS_VC = r7_NO_CONS_VC;
	}
	public BigDecimal getR7_NO_CON_FRA() {
		return R7_NO_CON_FRA;
	}
	public void setR7_NO_CON_FRA(BigDecimal r7_NO_CON_FRA) {
		R7_NO_CON_FRA = r7_NO_CON_FRA;
	}
	public BigDecimal getR7_NO_CON_MRK_FRD() {
		return R7_NO_CON_MRK_FRD;
	}
	public void setR7_NO_CON_MRK_FRD(BigDecimal r7_NO_CON_MRK_FRD) {
		R7_NO_CON_MRK_FRD = r7_NO_CON_MRK_FRD;
	}
	public BigDecimal getR7_TOT_CON_PRO_CBUAE() {
		return R7_TOT_CON_PRO_CBUAE;
	}
	public void setR7_TOT_CON_PRO_CBUAE(BigDecimal r7_TOT_CON_PRO_CBUAE) {
		R7_TOT_CON_PRO_CBUAE = r7_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR7_TOT_TRA_CBUAE() {
		return R7_TOT_TRA_CBUAE;
	}
	public void setR7_TOT_TRA_CBUAE(BigDecimal r7_TOT_TRA_CBUAE) {
		R7_TOT_TRA_CBUAE = r7_TOT_TRA_CBUAE;
	}
	public String getR8_BANK() {
		return R8_BANK;
	}
	public void setR8_BANK(String r8_BANK) {
		R8_BANK = r8_BANK;
	}
	public String getR8_PRODUCT() {
		return R8_PRODUCT;
	}
	public void setR8_PRODUCT(String r8_PRODUCT) {
		R8_PRODUCT = r8_PRODUCT;
	}
	public BigDecimal getR8_NO_PRO_SNR() {
		return R8_NO_PRO_SNR;
	}
	public void setR8_NO_PRO_SNR(BigDecimal r8_NO_PRO_SNR) {
		R8_NO_PRO_SNR = r8_NO_PRO_SNR;
	}
	public BigDecimal getR8_NO_TRA_SNR_SPT() {
		return R8_NO_TRA_SNR_SPT;
	}
	public void setR8_NO_TRA_SNR_SPT(BigDecimal r8_NO_TRA_SNR_SPT) {
		R8_NO_TRA_SNR_SPT = r8_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR8_NO_CONS_VC() {
		return R8_NO_CONS_VC;
	}
	public void setR8_NO_CONS_VC(BigDecimal r8_NO_CONS_VC) {
		R8_NO_CONS_VC = r8_NO_CONS_VC;
	}
	public BigDecimal getR8_NO_CON_FRA() {
		return R8_NO_CON_FRA;
	}
	public void setR8_NO_CON_FRA(BigDecimal r8_NO_CON_FRA) {
		R8_NO_CON_FRA = r8_NO_CON_FRA;
	}
	public BigDecimal getR8_NO_CON_MRK_FRD() {
		return R8_NO_CON_MRK_FRD;
	}
	public void setR8_NO_CON_MRK_FRD(BigDecimal r8_NO_CON_MRK_FRD) {
		R8_NO_CON_MRK_FRD = r8_NO_CON_MRK_FRD;
	}
	public BigDecimal getR8_TOT_CON_PRO_CBUAE() {
		return R8_TOT_CON_PRO_CBUAE;
	}
	public void setR8_TOT_CON_PRO_CBUAE(BigDecimal r8_TOT_CON_PRO_CBUAE) {
		R8_TOT_CON_PRO_CBUAE = r8_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR8_TOT_TRA_CBUAE() {
		return R8_TOT_TRA_CBUAE;
	}
	public void setR8_TOT_TRA_CBUAE(BigDecimal r8_TOT_TRA_CBUAE) {
		R8_TOT_TRA_CBUAE = r8_TOT_TRA_CBUAE;
	}
	public String getR9_BANK() {
		return R9_BANK;
	}
	public void setR9_BANK(String r9_BANK) {
		R9_BANK = r9_BANK;
	}
	public String getR9_PRODUCT() {
		return R9_PRODUCT;
	}
	public void setR9_PRODUCT(String r9_PRODUCT) {
		R9_PRODUCT = r9_PRODUCT;
	}
	public BigDecimal getR9_NO_PRO_SNR() {
		return R9_NO_PRO_SNR;
	}
	public void setR9_NO_PRO_SNR(BigDecimal r9_NO_PRO_SNR) {
		R9_NO_PRO_SNR = r9_NO_PRO_SNR;
	}
	public BigDecimal getR9_NO_TRA_SNR_SPT() {
		return R9_NO_TRA_SNR_SPT;
	}
	public void setR9_NO_TRA_SNR_SPT(BigDecimal r9_NO_TRA_SNR_SPT) {
		R9_NO_TRA_SNR_SPT = r9_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR9_NO_CONS_VC() {
		return R9_NO_CONS_VC;
	}
	public void setR9_NO_CONS_VC(BigDecimal r9_NO_CONS_VC) {
		R9_NO_CONS_VC = r9_NO_CONS_VC;
	}
	public BigDecimal getR9_NO_CON_FRA() {
		return R9_NO_CON_FRA;
	}
	public void setR9_NO_CON_FRA(BigDecimal r9_NO_CON_FRA) {
		R9_NO_CON_FRA = r9_NO_CON_FRA;
	}
	public BigDecimal getR9_NO_CON_MRK_FRD() {
		return R9_NO_CON_MRK_FRD;
	}
	public void setR9_NO_CON_MRK_FRD(BigDecimal r9_NO_CON_MRK_FRD) {
		R9_NO_CON_MRK_FRD = r9_NO_CON_MRK_FRD;
	}
	public BigDecimal getR9_TOT_CON_PRO_CBUAE() {
		return R9_TOT_CON_PRO_CBUAE;
	}
	public void setR9_TOT_CON_PRO_CBUAE(BigDecimal r9_TOT_CON_PRO_CBUAE) {
		R9_TOT_CON_PRO_CBUAE = r9_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR9_TOT_TRA_CBUAE() {
		return R9_TOT_TRA_CBUAE;
	}
	public void setR9_TOT_TRA_CBUAE(BigDecimal r9_TOT_TRA_CBUAE) {
		R9_TOT_TRA_CBUAE = r9_TOT_TRA_CBUAE;
	}
	public String getR10_BANK() {
		return R10_BANK;
	}
	public void setR10_BANK(String r10_BANK) {
		R10_BANK = r10_BANK;
	}
	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}
	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}
	public BigDecimal getR10_NO_PRO_SNR() {
		return R10_NO_PRO_SNR;
	}
	public void setR10_NO_PRO_SNR(BigDecimal r10_NO_PRO_SNR) {
		R10_NO_PRO_SNR = r10_NO_PRO_SNR;
	}
	public BigDecimal getR10_NO_TRA_SNR_SPT() {
		return R10_NO_TRA_SNR_SPT;
	}
	public void setR10_NO_TRA_SNR_SPT(BigDecimal r10_NO_TRA_SNR_SPT) {
		R10_NO_TRA_SNR_SPT = r10_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR10_NO_CONS_VC() {
		return R10_NO_CONS_VC;
	}
	public void setR10_NO_CONS_VC(BigDecimal r10_NO_CONS_VC) {
		R10_NO_CONS_VC = r10_NO_CONS_VC;
	}
	public BigDecimal getR10_NO_CON_FRA() {
		return R10_NO_CON_FRA;
	}
	public void setR10_NO_CON_FRA(BigDecimal r10_NO_CON_FRA) {
		R10_NO_CON_FRA = r10_NO_CON_FRA;
	}
	public BigDecimal getR10_NO_CON_MRK_FRD() {
		return R10_NO_CON_MRK_FRD;
	}
	public void setR10_NO_CON_MRK_FRD(BigDecimal r10_NO_CON_MRK_FRD) {
		R10_NO_CON_MRK_FRD = r10_NO_CON_MRK_FRD;
	}
	public BigDecimal getR10_TOT_CON_PRO_CBUAE() {
		return R10_TOT_CON_PRO_CBUAE;
	}
	public void setR10_TOT_CON_PRO_CBUAE(BigDecimal r10_TOT_CON_PRO_CBUAE) {
		R10_TOT_CON_PRO_CBUAE = r10_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR10_TOT_TRA_CBUAE() {
		return R10_TOT_TRA_CBUAE;
	}
	public void setR10_TOT_TRA_CBUAE(BigDecimal r10_TOT_TRA_CBUAE) {
		R10_TOT_TRA_CBUAE = r10_TOT_TRA_CBUAE;
	}
	public String getR11_BANK() {
		return R11_BANK;
	}
	public void setR11_BANK(String r11_BANK) {
		R11_BANK = r11_BANK;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_NO_PRO_SNR() {
		return R11_NO_PRO_SNR;
	}
	public void setR11_NO_PRO_SNR(BigDecimal r11_NO_PRO_SNR) {
		R11_NO_PRO_SNR = r11_NO_PRO_SNR;
	}
	public BigDecimal getR11_NO_TRA_SNR_SPT() {
		return R11_NO_TRA_SNR_SPT;
	}
	public void setR11_NO_TRA_SNR_SPT(BigDecimal r11_NO_TRA_SNR_SPT) {
		R11_NO_TRA_SNR_SPT = r11_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR11_NO_CONS_VC() {
		return R11_NO_CONS_VC;
	}
	public void setR11_NO_CONS_VC(BigDecimal r11_NO_CONS_VC) {
		R11_NO_CONS_VC = r11_NO_CONS_VC;
	}
	public BigDecimal getR11_NO_CON_FRA() {
		return R11_NO_CON_FRA;
	}
	public void setR11_NO_CON_FRA(BigDecimal r11_NO_CON_FRA) {
		R11_NO_CON_FRA = r11_NO_CON_FRA;
	}
	public BigDecimal getR11_NO_CON_MRK_FRD() {
		return R11_NO_CON_MRK_FRD;
	}
	public void setR11_NO_CON_MRK_FRD(BigDecimal r11_NO_CON_MRK_FRD) {
		R11_NO_CON_MRK_FRD = r11_NO_CON_MRK_FRD;
	}
	public BigDecimal getR11_TOT_CON_PRO_CBUAE() {
		return R11_TOT_CON_PRO_CBUAE;
	}
	public void setR11_TOT_CON_PRO_CBUAE(BigDecimal r11_TOT_CON_PRO_CBUAE) {
		R11_TOT_CON_PRO_CBUAE = r11_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR11_TOT_TRA_CBUAE() {
		return R11_TOT_TRA_CBUAE;
	}
	public void setR11_TOT_TRA_CBUAE(BigDecimal r11_TOT_TRA_CBUAE) {
		R11_TOT_TRA_CBUAE = r11_TOT_TRA_CBUAE;
	}
	public String getR12_BANK() {
		return R12_BANK;
	}
	public void setR12_BANK(String r12_BANK) {
		R12_BANK = r12_BANK;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_NO_PRO_SNR() {
		return R12_NO_PRO_SNR;
	}
	public void setR12_NO_PRO_SNR(BigDecimal r12_NO_PRO_SNR) {
		R12_NO_PRO_SNR = r12_NO_PRO_SNR;
	}
	public BigDecimal getR12_NO_TRA_SNR_SPT() {
		return R12_NO_TRA_SNR_SPT;
	}
	public void setR12_NO_TRA_SNR_SPT(BigDecimal r12_NO_TRA_SNR_SPT) {
		R12_NO_TRA_SNR_SPT = r12_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR12_NO_CONS_VC() {
		return R12_NO_CONS_VC;
	}
	public void setR12_NO_CONS_VC(BigDecimal r12_NO_CONS_VC) {
		R12_NO_CONS_VC = r12_NO_CONS_VC;
	}
	public BigDecimal getR12_NO_CON_FRA() {
		return R12_NO_CON_FRA;
	}
	public void setR12_NO_CON_FRA(BigDecimal r12_NO_CON_FRA) {
		R12_NO_CON_FRA = r12_NO_CON_FRA;
	}
	public BigDecimal getR12_NO_CON_MRK_FRD() {
		return R12_NO_CON_MRK_FRD;
	}
	public void setR12_NO_CON_MRK_FRD(BigDecimal r12_NO_CON_MRK_FRD) {
		R12_NO_CON_MRK_FRD = r12_NO_CON_MRK_FRD;
	}
	public BigDecimal getR12_TOT_CON_PRO_CBUAE() {
		return R12_TOT_CON_PRO_CBUAE;
	}
	public void setR12_TOT_CON_PRO_CBUAE(BigDecimal r12_TOT_CON_PRO_CBUAE) {
		R12_TOT_CON_PRO_CBUAE = r12_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR12_TOT_TRA_CBUAE() {
		return R12_TOT_TRA_CBUAE;
	}
	public void setR12_TOT_TRA_CBUAE(BigDecimal r12_TOT_TRA_CBUAE) {
		R12_TOT_TRA_CBUAE = r12_TOT_TRA_CBUAE;
	}
	public String getR13_BANK() {
		return R13_BANK;
	}
	public void setR13_BANK(String r13_BANK) {
		R13_BANK = r13_BANK;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_NO_PRO_SNR() {
		return R13_NO_PRO_SNR;
	}
	public void setR13_NO_PRO_SNR(BigDecimal r13_NO_PRO_SNR) {
		R13_NO_PRO_SNR = r13_NO_PRO_SNR;
	}
	public BigDecimal getR13_NO_TRA_SNR_SPT() {
		return R13_NO_TRA_SNR_SPT;
	}
	public void setR13_NO_TRA_SNR_SPT(BigDecimal r13_NO_TRA_SNR_SPT) {
		R13_NO_TRA_SNR_SPT = r13_NO_TRA_SNR_SPT;
	}
	public BigDecimal getR13_NO_CONS_VC() {
		return R13_NO_CONS_VC;
	}
	public void setR13_NO_CONS_VC(BigDecimal r13_NO_CONS_VC) {
		R13_NO_CONS_VC = r13_NO_CONS_VC;
	}
	public BigDecimal getR13_NO_CON_FRA() {
		return R13_NO_CON_FRA;
	}
	public void setR13_NO_CON_FRA(BigDecimal r13_NO_CON_FRA) {
		R13_NO_CON_FRA = r13_NO_CON_FRA;
	}
	public BigDecimal getR13_NO_CON_MRK_FRD() {
		return R13_NO_CON_MRK_FRD;
	}
	public void setR13_NO_CON_MRK_FRD(BigDecimal r13_NO_CON_MRK_FRD) {
		R13_NO_CON_MRK_FRD = r13_NO_CON_MRK_FRD;
	}
	public BigDecimal getR13_TOT_CON_PRO_CBUAE() {
		return R13_TOT_CON_PRO_CBUAE;
	}
	public void setR13_TOT_CON_PRO_CBUAE(BigDecimal r13_TOT_CON_PRO_CBUAE) {
		R13_TOT_CON_PRO_CBUAE = r13_TOT_CON_PRO_CBUAE;
	}
	public BigDecimal getR13_TOT_TRA_CBUAE() {
		return R13_TOT_TRA_CBUAE;
	}
	public void setR13_TOT_TRA_CBUAE(BigDecimal r13_TOT_TRA_CBUAE) {
		R13_TOT_TRA_CBUAE = r13_TOT_TRA_CBUAE;
	}
	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}
	public void setREPORT_DATE(Date rEPORT_DATE) {
		REPORT_DATE = rEPORT_DATE;
	}
	public String getBRANCH_CODE() {
		return BRANCH_CODE;
	}
	public void setBRANCH_CODE(String bRANCH_CODE) {
		BRANCH_CODE = bRANCH_CODE;
	}
	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}
	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}
	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}
	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}
	public String getDEL_FLG() {
		return DEL_FLG;
	}
	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}
	public String getSOLID() {
		return SOLID;
	}
	public void setSOLID(String sOLID) {
		SOLID = sOLID;
	}
	public String getBRANCH_NAME() {
		return BRANCH_NAME;
	}
	public void setBRANCH_NAME(String bRANCH_NAME) {
		BRANCH_NAME = bRANCH_NAME;
	}
	public String getMODIFY_USERID() {
		return MODIFY_USERID;
	}
	public void setMODIFY_USERID(String mODIFY_USERID) {
		MODIFY_USERID = mODIFY_USERID;
	}
	public String getVERIFY_FLG() {
		return VERIFY_FLG;
	}
	public void setVERIFY_FLG(String vERIFY_FLG) {
		VERIFY_FLG = vERIFY_FLG;
	}
	public RT_MC_TABLE6_ENTITY(String r7_BANK, String r7_PRODUCT, BigDecimal r7_NO_PRO_SNR,
			BigDecimal r7_NO_TRA_SNR_SPT, BigDecimal r7_NO_CONS_VC, BigDecimal r7_NO_CON_FRA,
			BigDecimal r7_NO_CON_MRK_FRD, BigDecimal r7_TOT_CON_PRO_CBUAE, BigDecimal r7_TOT_TRA_CBUAE, String r8_BANK,
			String r8_PRODUCT, BigDecimal r8_NO_PRO_SNR, BigDecimal r8_NO_TRA_SNR_SPT, BigDecimal r8_NO_CONS_VC,
			BigDecimal r8_NO_CON_FRA, BigDecimal r8_NO_CON_MRK_FRD, BigDecimal r8_TOT_CON_PRO_CBUAE,
			BigDecimal r8_TOT_TRA_CBUAE, String r9_BANK, String r9_PRODUCT, BigDecimal r9_NO_PRO_SNR,
			BigDecimal r9_NO_TRA_SNR_SPT, BigDecimal r9_NO_CONS_VC, BigDecimal r9_NO_CON_FRA,
			BigDecimal r9_NO_CON_MRK_FRD, BigDecimal r9_TOT_CON_PRO_CBUAE, BigDecimal r9_TOT_TRA_CBUAE, String r10_BANK,
			String r10_PRODUCT, BigDecimal r10_NO_PRO_SNR, BigDecimal r10_NO_TRA_SNR_SPT, BigDecimal r10_NO_CONS_VC,
			BigDecimal r10_NO_CON_FRA, BigDecimal r10_NO_CON_MRK_FRD, BigDecimal r10_TOT_CON_PRO_CBUAE,
			BigDecimal r10_TOT_TRA_CBUAE, String r11_BANK, String r11_PRODUCT, BigDecimal r11_NO_PRO_SNR,
			BigDecimal r11_NO_TRA_SNR_SPT, BigDecimal r11_NO_CONS_VC, BigDecimal r11_NO_CON_FRA,
			BigDecimal r11_NO_CON_MRK_FRD, BigDecimal r11_TOT_CON_PRO_CBUAE, BigDecimal r11_TOT_TRA_CBUAE,
			String r12_BANK, String r12_PRODUCT, BigDecimal r12_NO_PRO_SNR, BigDecimal r12_NO_TRA_SNR_SPT,
			BigDecimal r12_NO_CONS_VC, BigDecimal r12_NO_CON_FRA, BigDecimal r12_NO_CON_MRK_FRD,
			BigDecimal r12_TOT_CON_PRO_CBUAE, BigDecimal r12_TOT_TRA_CBUAE, String r13_BANK, String r13_PRODUCT,
			BigDecimal r13_NO_PRO_SNR, BigDecimal r13_NO_TRA_SNR_SPT, BigDecimal r13_NO_CONS_VC,
			BigDecimal r13_NO_CON_FRA, BigDecimal r13_NO_CON_MRK_FRD, BigDecimal r13_TOT_CON_PRO_CBUAE,
			BigDecimal r13_TOT_TRA_CBUAE, Date rEPORT_DATE, String bRANCH_CODE, String eNTITY_FLG, String mODIFY_FLG,
			String dEL_FLG, String sOLID, String bRANCH_NAME, String mODIFY_USERID, String vERIFY_FLG) {
		super();
		R7_BANK = r7_BANK;
		R7_PRODUCT = r7_PRODUCT;
		R7_NO_PRO_SNR = r7_NO_PRO_SNR;
		R7_NO_TRA_SNR_SPT = r7_NO_TRA_SNR_SPT;
		R7_NO_CONS_VC = r7_NO_CONS_VC;
		R7_NO_CON_FRA = r7_NO_CON_FRA;
		R7_NO_CON_MRK_FRD = r7_NO_CON_MRK_FRD;
		R7_TOT_CON_PRO_CBUAE = r7_TOT_CON_PRO_CBUAE;
		R7_TOT_TRA_CBUAE = r7_TOT_TRA_CBUAE;
		R8_BANK = r8_BANK;
		R8_PRODUCT = r8_PRODUCT;
		R8_NO_PRO_SNR = r8_NO_PRO_SNR;
		R8_NO_TRA_SNR_SPT = r8_NO_TRA_SNR_SPT;
		R8_NO_CONS_VC = r8_NO_CONS_VC;
		R8_NO_CON_FRA = r8_NO_CON_FRA;
		R8_NO_CON_MRK_FRD = r8_NO_CON_MRK_FRD;
		R8_TOT_CON_PRO_CBUAE = r8_TOT_CON_PRO_CBUAE;
		R8_TOT_TRA_CBUAE = r8_TOT_TRA_CBUAE;
		R9_BANK = r9_BANK;
		R9_PRODUCT = r9_PRODUCT;
		R9_NO_PRO_SNR = r9_NO_PRO_SNR;
		R9_NO_TRA_SNR_SPT = r9_NO_TRA_SNR_SPT;
		R9_NO_CONS_VC = r9_NO_CONS_VC;
		R9_NO_CON_FRA = r9_NO_CON_FRA;
		R9_NO_CON_MRK_FRD = r9_NO_CON_MRK_FRD;
		R9_TOT_CON_PRO_CBUAE = r9_TOT_CON_PRO_CBUAE;
		R9_TOT_TRA_CBUAE = r9_TOT_TRA_CBUAE;
		R10_BANK = r10_BANK;
		R10_PRODUCT = r10_PRODUCT;
		R10_NO_PRO_SNR = r10_NO_PRO_SNR;
		R10_NO_TRA_SNR_SPT = r10_NO_TRA_SNR_SPT;
		R10_NO_CONS_VC = r10_NO_CONS_VC;
		R10_NO_CON_FRA = r10_NO_CON_FRA;
		R10_NO_CON_MRK_FRD = r10_NO_CON_MRK_FRD;
		R10_TOT_CON_PRO_CBUAE = r10_TOT_CON_PRO_CBUAE;
		R10_TOT_TRA_CBUAE = r10_TOT_TRA_CBUAE;
		R11_BANK = r11_BANK;
		R11_PRODUCT = r11_PRODUCT;
		R11_NO_PRO_SNR = r11_NO_PRO_SNR;
		R11_NO_TRA_SNR_SPT = r11_NO_TRA_SNR_SPT;
		R11_NO_CONS_VC = r11_NO_CONS_VC;
		R11_NO_CON_FRA = r11_NO_CON_FRA;
		R11_NO_CON_MRK_FRD = r11_NO_CON_MRK_FRD;
		R11_TOT_CON_PRO_CBUAE = r11_TOT_CON_PRO_CBUAE;
		R11_TOT_TRA_CBUAE = r11_TOT_TRA_CBUAE;
		R12_BANK = r12_BANK;
		R12_PRODUCT = r12_PRODUCT;
		R12_NO_PRO_SNR = r12_NO_PRO_SNR;
		R12_NO_TRA_SNR_SPT = r12_NO_TRA_SNR_SPT;
		R12_NO_CONS_VC = r12_NO_CONS_VC;
		R12_NO_CON_FRA = r12_NO_CON_FRA;
		R12_NO_CON_MRK_FRD = r12_NO_CON_MRK_FRD;
		R12_TOT_CON_PRO_CBUAE = r12_TOT_CON_PRO_CBUAE;
		R12_TOT_TRA_CBUAE = r12_TOT_TRA_CBUAE;
		R13_BANK = r13_BANK;
		R13_PRODUCT = r13_PRODUCT;
		R13_NO_PRO_SNR = r13_NO_PRO_SNR;
		R13_NO_TRA_SNR_SPT = r13_NO_TRA_SNR_SPT;
		R13_NO_CONS_VC = r13_NO_CONS_VC;
		R13_NO_CON_FRA = r13_NO_CON_FRA;
		R13_NO_CON_MRK_FRD = r13_NO_CON_MRK_FRD;
		R13_TOT_CON_PRO_CBUAE = r13_TOT_CON_PRO_CBUAE;
		R13_TOT_TRA_CBUAE = r13_TOT_TRA_CBUAE;
		REPORT_DATE = rEPORT_DATE;
		BRANCH_CODE = bRANCH_CODE;
		ENTITY_FLG = eNTITY_FLG;
		MODIFY_FLG = mODIFY_FLG;
		DEL_FLG = dEL_FLG;
		SOLID = sOLID;
		BRANCH_NAME = bRANCH_NAME;
		MODIFY_USERID = mODIFY_USERID;
		VERIFY_FLG = vERIFY_FLG;
	}
	public RT_MC_TABLE6_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

}
