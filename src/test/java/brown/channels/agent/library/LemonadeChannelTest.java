package brown.channels.agent.library;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import brown.channels.MechanismType;
import brown.exceptions.AgentCreationException;
import brown.mechanism.channel.GameChannel;
import brown.platform.accounting.Ledger;
import brown.platform.messages.BankUpdateMessage;
import brown.platform.messages.TradeMessage;
import brown.platform.messages.TradeRequestMessage;
import brown.system.setup.ISetup;
import brown.system.setup.LemonadeSetup;
import brown.system.setup.Startup;
import brown.user.agent.AbsLemonadeAgent;

//this test assures that the lemonade channel both sends messages to the server and 
//interacts appropriately with a lemonade agent. It will impersonate a server and a lemonade agent. 
public class LemonadeChannelTest {
   
private class TestServer {
    
  private String myMessage; 
  
    public TestServer(int port, ISetup GameSetup) throws IOException {
      Server server = new Server(); 
      Kryo serverKryo = server.getKryo();
      Startup.start(serverKryo);
      server.bind(port, port);
      server.start(); 
      server.addListener(new Listener() {
        public void received (Connection connection, Object object) {
          //post responses here.
          if (object.equals("send me a trade request")) {
            connection.sendTCP(new TradeRequestMessage(0,
                new GameChannel(0, new Ledger(0), null, MechanismType.Lemonade), MechanismType.Lemonade));
          } else if (object instanceof TradeMessage) { 
            setMessage();
          }
        }
     });
    }
    
    private void setMessage() {
      this.myMessage = "bid received- lemonade channel functional.";
    }
    
    public String getMessage() {
      return this.myMessage;
    }
  }
  
  private class MockLemonadeAgent extends AbsLemonadeAgent {

    public MockLemonadeAgent(String host, int port, ISetup gameSetup)
        throws AgentCreationException {
      super(host, port, gameSetup);
      // This agent will receive the channel and then dispatch message will be called.
      // this will invoke the channel to invoke onLemonade.
      // TODO Auto-generated constructor stub
    }

    @Override
    public void onLemonade(GameChannel channel) {
      //when the agent receives a lemonade trade request, it is directed 
      //to onLemonade. This invokes the 
      channel.bid(this, 10);
    }

    @Override
    public void onBankUpdate(BankUpdateMessage bankUpdate) {
      // Noop here
    } 
    
  }
  
  //testing that the agent has received a message, and that the channel acts appropriately
  //to send a response.
  @Test
  public void testLemonadeChannel() throws AgentCreationException, IOException, InterruptedException {
    TestServer ts = new TestServer(2121, new LemonadeSetup());
    MockLemonadeAgent m = new MockLemonadeAgent("localhost", 2121, new LemonadeSetup());
    m.CLIENT.sendTCP("send me a trade request");
    Thread.sleep(100);
    assertEquals(ts.getMessage(), "bid received- lemonade channel functional."); 
  }
}