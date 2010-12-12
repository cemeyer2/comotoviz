package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: 12/11/10
 *
 * <p> <p> An object holding the information associated with an offering
 */
public class OfferingInfo{

    /**
     * Extra offering information from the API
     */
    private List<String> cn;
    private List<String> distinguishedName;
    private List<String> extensionAttribute1;
    private List<String> extensionAttribute2;
    private List<String> extensionAttribute3;
    private List<String> extensionAttribute4;
    private List<String> extensionAttribute5;
    private List<String> extensionAttribute6;
    private List<String> extensionAttribute7;
    private List<String> groupType;
    private List<String> instanceType;
    private List<String> member;
    private List<String> name;
    private List<String> objectCategory;
    private List<String> objectClass;
    private List<String> objectGUID;
    private List<String> objectSid;
    private List<String> sAMAccountName;
    private List<String> sAMAccountType;
    private List<String> uSNChanged;
    private List<String> uSNCreated;
    private List<String> whenChanged;
    private List<String> whenCreated;

    /**
     * The object holding the connection data for the API
     */
    private CoMoToAPIConnection connection;

    /**
     * Creates an 'OfferingInfo' object
     *
     * @param abstractOfferingInfo A map holding the data of this object
     * @param connection The connection to the API
     */
    public OfferingInfo(Map abstractOfferingInfo, CoMoToAPIConnection connection) {

        //Save the connections
        this.connection = connection;

        //Populate this object using reflection
        CoMoToAPIReflector<OfferingInfo> reflector = new CoMoToAPIReflector<OfferingInfo>();
        reflector.populate(this, abstractOfferingInfo);
    }

    public List<String> getCn() {
        return cn;
    }

    public void setCn(List<String> cn) {
        this.cn = cn;
    }

    public List<String> getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(List<String> distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public List<String> getExtensionAttribute1() {
        return extensionAttribute1;
    }

    public void setExtensionAttribute1(List<String> extensionAttribute1) {
        this.extensionAttribute1 = extensionAttribute1;
    }

    public List<String> getExtensionAttribute2() {
        return extensionAttribute2;
    }

    public void setExtensionAttribute2(List<String> extensionAttribute2) {
        this.extensionAttribute2 = extensionAttribute2;
    }

    public List<String> getExtensionAttribute3() {
        return extensionAttribute3;
    }

    public void setExtensionAttribute3(List<String> extensionAttribute3) {
        this.extensionAttribute3 = extensionAttribute3;
    }

    public List<String> getExtensionAttribute4() {
        return extensionAttribute4;
    }

    public void setExtensionAttribute4(List<String> extensionAttribute4) {
        this.extensionAttribute4 = extensionAttribute4;
    }

    public List<String> getExtensionAttribute5() {
        return extensionAttribute5;
    }

    public void setExtensionAttribute5(List<String> extensionAttribute5) {
        this.extensionAttribute5 = extensionAttribute5;
    }

    public List<String> getExtensionAttribute6() {
        return extensionAttribute6;
    }

    public void setExtensionAttribute6(List<String> extensionAttribute6) {
        this.extensionAttribute6 = extensionAttribute6;
    }

    public List<String> getExtensionAttribute7() {
        return extensionAttribute7;
    }

    public void setExtensionAttribute7(List<String> extensionAttribute7) {
        this.extensionAttribute7 = extensionAttribute7;
    }

    public List<String> getGroupType() {
        return groupType;
    }

    public void setGroupType(List<String> groupType) {
        this.groupType = groupType;
    }

    public List<String> getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(List<String> instanceType) {
        this.instanceType = instanceType;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(List<String> objectCategory) {
        this.objectCategory = objectCategory;
    }

    public List<String> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(List<String> objectClass) {
        this.objectClass = objectClass;
    }

    public List<String> getObjectGUID() {
        return objectGUID;
    }

    public void setObjectGUID(List<String> objectGUID) {
        this.objectGUID = objectGUID;
    }

    public List<String> getObjectSid() {
        return objectSid;
    }

    public void setObjectSid(List<String> objectSid) {
        this.objectSid = objectSid;
    }

    public List<String> getsAMAccountName() {
        return sAMAccountName;
    }

    public void setsAMAccountName(List<String> sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
    }

    public List<String> getsAMAccountType() {
        return sAMAccountType;
    }

    public void setsAMAccountType(List<String> sAMAccountType) {
        this.sAMAccountType = sAMAccountType;
    }

    public List<String> getuSNChanged() {
        return uSNChanged;
    }

    public void setuSNChanged(List<String> uSNChanged) {
        this.uSNChanged = uSNChanged;
    }

    public List<String> getuSNCreated() {
        return uSNCreated;
    }

    public void setuSNCreated(List<String> uSNCreated) {
        this.uSNCreated = uSNCreated;
    }

    public List<String> getWhenChanged() {
        return whenChanged;
    }

    public void setWhenChanged(List<String> whenChanged) {
        this.whenChanged = whenChanged;
    }

    public List<String> getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(List<String> whenCreated) {
        this.whenCreated = whenCreated;
    }
}
