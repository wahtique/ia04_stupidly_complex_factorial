/**
 * @author William
 * @since 06/03/2018
 */

package model;

public class FactProblem
{
    int factN;
    long factResult;

    public FactProblem(int factN, long factResult)
    {
        this.factN = factN;
        this.factResult = factResult;
    }

    public int getFactN()
    {
        return factN;
    }

    public long getFactResult()
    {
        return factResult;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !FactProblem.class.isAssignableFrom(obj.getClass()))
        {
            return false;
        }
        FactProblem tmp = (FactProblem) obj;
        return factN == tmp.getFactN();
    }
}
