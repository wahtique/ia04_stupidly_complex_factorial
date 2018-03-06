/**
 * @author William
 * @since 06/03/2018
 */

package behaviour;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class ParallelScheduler extends ParallelBehaviour
{
    public ParallelScheduler(Agent a, int endCondition)
    {
        super(a, endCondition);
    }

    @Override
    protected boolean checkTermination(boolean currentDone, int currentResult)
    {
        return false;
    }
}
