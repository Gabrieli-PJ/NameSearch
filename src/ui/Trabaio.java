package ui;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Trabaio extends FlatDarculaLaf {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Trabaio";

	public static boolean setup() {
		return setup( new Trabaio() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Trabaio.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
