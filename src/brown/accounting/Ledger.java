package brown.accounting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.accounting.bidbundle.Allocation;
import brown.tradeable.library.Tradeable;

/**
 * A ledger tracks all trades within a Market. 
 * @author lcamery
 *
 */
public class Ledger {
  protected final Integer marketId;
	protected final List<Transaction> transactions;
	protected final Map<Tradeable, Transaction> latest;
	protected final List<Transaction> unshared;
	
	/**
	 * For Kryo do not use
	 */
	public Ledger() {
	  this.marketId = null;
		this.transactions = null;
		this.latest = null;
		this.unshared = null;
	}
	

	 public Ledger(Integer marketId) {
	    this.marketId = marketId; 
	    this.unshared = new LinkedList<Transaction>();
	    this.transactions = new LinkedList<Transaction>();
	    this.latest = new HashMap<Tradeable, Transaction>();
	  }
	 
	 /**
	  * for convenience of implementation in the market class.
	  * @param marketId
	  * @param initialAlloc
	  */
	 public Ledger(Integer marketId, Allocation initialAlloc) {
     this.marketId = marketId; 
     this.unshared = new LinkedList<Transaction>();
     this.transactions = new LinkedList<Transaction>();
     this.latest = new HashMap<Tradeable, Transaction>();
     this.addAll(initialAlloc);
   }

	/**
	 * Adds a transaction
	 * @param t : transaction to add
	 */
	public void add(Transaction t) {
		synchronized(transactions) {
			this.latest.put(t.TRADEABLE,t);
			this.transactions.add(t);
			this.unshared.add(t);
		}
	}
	
	public void addAll(Allocation bids) {
	  if (bids != null) {
	    for (Entry<Tradeable, MarketState> t : bids.getBids().bids.entrySet()) { 
	      Transaction tr = new Transaction(t.getValue().AGENTID, null, t.getValue().PRICE, 1, t.getKey());
	      this.latest.put(t.getKey(), tr);
	      this.transactions.add(tr); 
	      this.unshared.add(tr);
	    }
	  }
	}
	
	/**
	 * Constructs a set of all transactions
	 * @return set
	 */
	public List<Transaction> getList() {
		return new LinkedList<Transaction>(this.transactions);
	}
	
	/**
	 * Gets the latest transactions
	 * @return
	 */
	public Set<Transaction> getLatest() {
		return new HashSet<Transaction>(this.latest.values());
	}

	/**
	 * Adds a list of transactions
	 * @param trans
	 */
	public void add(List<Transaction> trans) {
		synchronized(transactions) {
			for (Transaction t : trans) {
				this.latest.put(t.TRADEABLE, t);
			}
			this.transactions.addAll(trans);
			this.unshared.addAll(trans);
		}
	}
	
	/**
	 * Gets the ledger without others' IDs
	 * @param ID : this agent's ID
	 * @return ledger
	 */
	public Ledger getSanitized(Integer ID) {
		Ledger ledger = new Ledger(null);
		synchronized(transactions) {
			for (Transaction t : this.unshared) {
				ledger.add(t.sanitize(ID));
			}
		}
		return ledger;
	}
	
	/**
	 * Clears the latest set
	 */
	public void clearLatest() {
		this.unshared.clear();
	}

}