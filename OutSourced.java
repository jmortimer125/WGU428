package software.wgu428;
/** outsourced creates a construct with a company name string instead of a machine id.
 *
 */

public class OutSourced extends Parts {

    private String companyName;

    public OutSourced(int partID, int stock, int min, int max, String name, double partCost, String companyName) {
        super(partID, stock, min, max, name, partCost);

        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
