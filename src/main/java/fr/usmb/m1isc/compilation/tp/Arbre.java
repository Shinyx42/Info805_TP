package fr.usmb.m1isc.compilation.tp;

public class Arbre {
	private NodeType type;
	private Arbre fg, fd;
	private String value;
	
	public Arbre(NodeType type, Arbre fg, Arbre fd) {
		this.type=type;
		convertType();
		this.fd=fd;
		this.fg=fg;
		
	}
	
	public Arbre(NodeType type, String value) {
		this.type=type;
		convertType();
		this.value=value;
		this.fg=null;
		this.fd=null;
	}
	
	public Arbre() {
		// TODO Auto-generated constructor stub
	}

	public Arbre(NodeType type, Object e1, Object e2) {
		this(type, (Arbre) e1, (Arbre) e2);
	}

	public Arbre(NodeType type, Object e) {
		this(type, null,  (Arbre) e);
	}
	
	public Arbre(NodeType type, Integer n) {
		this(type, n.toString());
	}

	public Arbre(NodeType type) {
		this(type, null, null);
	}

	private void convertType() {
		switch(type) {
		case AND:
			value="AND";
			break;
		case DIV:
			value="/";
			break;
		case DO:
			value="DO";
			break;
		case EGAL:
			value="=";
			break;
		case ELSE:
			value="ELSE";
			break;
		case ERROR:
			break;
		case GT:
			value="<";
			break;
		case GTE:
			value="<=";
			break;
		case IF:
			value="IF";
			break;
		case OUTPUT:
			value="OUTPUT";
			break;
		case INPUT:
			value="INPUT";
			break;
		case LET:
			value="LET";
			break;
		case MOD:
			value="MOD";
			break;
		case MOINS:
		case MOINS_UNAIRE:
			value="-";
			break;
		case MUL:
			value="*";
			break;
		case NIL:
			value="NIL";
			break;
		case NOT:
			value="NOT";
			break;
		case OR:
			value="OR";
			break;
		case PLUS:
			value="+";
			break;
		case SEMI:
			value=";";
			break;
		case THEN:
			value="THEN";
			break;
		case WHILE:
			value="WHILE";
			break;
		default:
			break;
		
		}
	}
	
	@Override
	public String toString() {
		if(type == NodeType.IDENT || type == NodeType.ENTIER) return value;
		String result="("+value+" ";
		if(fg!=null) result+= fg+" ";
		if(fd!=null) result+= fd;
		result+=")";
		return result;
	}

	public NodeType getType() {
		return type;
	}

	public Arbre getFg() {
		return fg;
	}
	public Arbre getFd() {
		return fd;
	}

	public String getValue() {
		return value;
	}
}
