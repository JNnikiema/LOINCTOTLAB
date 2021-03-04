package utilitaries;

public class CoupleCorrection {
	  public final int x;
	  public final String y;
	  public CoupleCorrection(int x, String y) {
				this.x = x;
					this.y = y;
				}

	  @Override
	  public boolean equals(Object o_) {
		  CoupleCorrection o = (CoupleCorrection) o_;

		  return (o.x==x && o.y.equals(y));
	  }

	  @Override
	  public int hashCode()
	  {
		  return 31 * x + y.hashCode();
	  }
}
