package brown.market.preset;

import brown.rules.IActivityRule;
import brown.rules.IAllocationRule;
import brown.rules.IGroupingRule;
import brown.rules.IInformationRevelationPolicy;
import brown.rules.IInnerTC;
import brown.rules.IOuterTC;
import brown.rules.IPaymentRule;
import brown.rules.IQueryRule;
import brown.rules.IRecordKeepingRule;

/**
 * Describes all the rules for a particular market.
 * These rules fully describe the behavior of the auction.
 * @author acoggins
 *
 */
public abstract class AbsMarketPreset {
  
  public IAllocationRule aRule; 
  public IPaymentRule pRule; 
  public IQueryRule qRule;
  public IActivityRule actRule; 
  public IInformationRevelationPolicy infoPolicy;
  public IInnerTC innerTCondition; 
  public IOuterTC outerTCondition;
  public IGroupingRule gRule; 
  public IRecordKeepingRule rRule;
  
  public AbsMarketPreset(IAllocationRule aRule, IPaymentRule pRule, IQueryRule qRule, IGroupingRule gRule,
      IActivityRule oneShotActivity, IInformationRevelationPolicy infoPolicy, IInnerTC innerTCondition, 
      IOuterTC outerTCondition, IRecordKeepingRule rRule) {
    this.aRule = aRule; 
    this.pRule = pRule; 
    this.qRule = qRule; 
    this.gRule = gRule;
    this.actRule = oneShotActivity; 
    this.infoPolicy = infoPolicy; 
    this.innerTCondition = innerTCondition; 
    this.outerTCondition = outerTCondition; 
    this.rRule = rRule;
  }
  
  public abstract AbsMarketPreset copy();
}