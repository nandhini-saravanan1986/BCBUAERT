package com.bornfire.xbrl.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MIS_TREASURY_PLACEMENT") // replace with your actual table name
public class MIS_TREASURY_PLACEMENT_ENTITY {

    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date reportDate;

    @Id
    @Column(name = "NUM_OPERATION", length = 50)
    private String numOperation;

    @Column(name = "ENTITE_OPERATION", length = 100)
    private String entiteOperation;

    @Column(name = "POSTE", length = 100)
    private String poste;

    @Column(name = "TITRE", length = 200)
    private String titre;

    @Column(name = "DEVISE_1", length = 10)
    private String devise1;

    @Column(name = "NOMINAL_1", precision = 26, scale = 2)
    private Double nominal1;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OPERATION")
    private Date dateOperation;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALEUR")
    private Date dateValeur;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_ECHEANCE")
    private Date dateEcheance;

    @Column(name = "PREAVIS", length = 100)
    private String preavis;

    @Column(name = "ENTITE", length = 100)
    private String entite;

    @Column(name = "PORTEFEUILLE", length = 100)
    private String portefeuille;

    @Column(name = "CONTREPARTIE", length = 200)
    private String contrepartie;

    @Column(name = "STATUT", length = 50)
    private String statut;

    @Column(name = "VALEUR_TAUX_1", precision = 26, scale = 2)
    private Double valeurTaux1;

    @Column(name = "TAUX_1", length = 50)
    private String taux1;

    @Column(name = "PERIODICITE_1", length = 50)
    private String periodicite1;

    @Column(name = "AFFAIRE", length = 100)
    private String affaire;

    @Column(name = "DUREE_INIT")
    private Integer dureeInit;

    @Column(name = "DUREE_RESTANT")
    private Integer dureeRestant;

    @Column(name = "TRI_1", length = 50)
    private String tri1;

    @Column(name = "TRI_2", length = 50)
    private String tri2;

    @Column(name = "TRI_3", length = 50)
    private String tri3;

    @Column(name = "OP_REFERENCE", length = 100)
    private String opReference;

    @Column(name = "FILTRAGE", length = 50)
    private String filtrage;

    @Column(name = "SIGNE", length = 10)
    private String signe;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FIN")
    private Date dateFin;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_DEBUT")
    private Date dateDebut;

    @Column(name = "OP_FINANCE", length = 100)
    private String opFinance;

    @Column(name = "CREATE_USER", length = 10)
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "MODIFY_USER", length = 50)
    private String modifyUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @Column(name = "VERIFY_USER", length = 10)
    private String verifyUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VERIFY_TIME")
    private Date verifyTime;

    @Column(name = "ENTITY_FLG", length = 1)
    private String entityFlg;

    @Column(name = "MODIFY_FLG", length = 1)
    private String modifyFlg;

    @Column(name = "BRANCH_CODE", length = 50)
    private String branchCode;

    @Column(name = "BRANCH_NAME", length = 50)
    private String branchName;

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getNumOperation() {
		return numOperation;
	}

	public void setNumOperation(String numOperation) {
		this.numOperation = numOperation;
	}

	public String getEntiteOperation() {
		return entiteOperation;
	}

	public void setEntiteOperation(String entiteOperation) {
		this.entiteOperation = entiteOperation;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDevise1() {
		return devise1;
	}

	public void setDevise1(String devise1) {
		this.devise1 = devise1;
	}

	public Double getNominal1() {
		return nominal1;
	}

	public void setNominal1(Double nominal1) {
		this.nominal1 = nominal1;
	}

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date dateOperation) {
		this.dateOperation = dateOperation;
	}

	public Date getDateValeur() {
		return dateValeur;
	}

	public void setDateValeur(Date dateValeur) {
		this.dateValeur = dateValeur;
	}

	public Date getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public String getPreavis() {
		return preavis;
	}

	public void setPreavis(String preavis) {
		this.preavis = preavis;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	public String getPortefeuille() {
		return portefeuille;
	}

	public void setPortefeuille(String portefeuille) {
		this.portefeuille = portefeuille;
	}

	public String getContrepartie() {
		return contrepartie;
	}

	public void setContrepartie(String contrepartie) {
		this.contrepartie = contrepartie;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Double getValeurTaux1() {
		return valeurTaux1;
	}

	public void setValeurTaux1(Double valeurTaux1) {
		this.valeurTaux1 = valeurTaux1;
	}

	public String getTaux1() {
		return taux1;
	}

	public void setTaux1(String taux1) {
		this.taux1 = taux1;
	}

	public String getPeriodicite1() {
		return periodicite1;
	}

	public void setPeriodicite1(String periodicite1) {
		this.periodicite1 = periodicite1;
	}

	public String getAffaire() {
		return affaire;
	}

	public void setAffaire(String affaire) {
		this.affaire = affaire;
	}

	public Integer getDureeInit() {
		return dureeInit;
	}

	public void setDureeInit(Integer dureeInit) {
		this.dureeInit = dureeInit;
	}

	public Integer getDureeRestant() {
		return dureeRestant;
	}

	public void setDureeRestant(Integer dureeRestant) {
		this.dureeRestant = dureeRestant;
	}

	public String getTri1() {
		return tri1;
	}

	public void setTri1(String tri1) {
		this.tri1 = tri1;
	}

	public String getTri2() {
		return tri2;
	}

	public void setTri2(String tri2) {
		this.tri2 = tri2;
	}

	public String getTri3() {
		return tri3;
	}

	public void setTri3(String tri3) {
		this.tri3 = tri3;
	}

	public String getOpReference() {
		return opReference;
	}

	public void setOpReference(String opReference) {
		this.opReference = opReference;
	}

	public String getFiltrage() {
		return filtrage;
	}

	public void setFiltrage(String filtrage) {
		this.filtrage = filtrage;
	}

	public String getSigne() {
		return signe;
	}

	public void setSigne(String signe) {
		this.signe = signe;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getOpFinance() {
		return opFinance;
	}

	public void setOpFinance(String opFinance) {
		this.opFinance = opFinance;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

    // Getters and setters...
    
}
