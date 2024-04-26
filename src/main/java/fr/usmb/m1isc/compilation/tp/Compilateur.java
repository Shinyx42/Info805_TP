package fr.usmb.m1isc.compilation.tp;

import java.util.HashSet;
import java.util.Set;

public class Compilateur {
	private Arbre a;
	private Set<String> dataListe;
	private Integer idWhile = 0;
	private Integer idGt    = 0;
	private Integer idGte   = 0;
	private Integer idIf	= 0;
	private Integer idOpBin = 0;
	
	public Compilateur(Arbre a) {
		this.a=a;
		dataListe= new HashSet<String>();
	}
	
	public String generer() {
		String code = "\nCODE SEGMENT\n"+generer(a)+"\nCODE ENDS";
		String data = "DATA SEGMENT";
		for(String s: dataListe) data+="\n\t"+s+" DD";
		data+= "\nDATA ENDS";
		return data+code;
	}
	
	public String generer(Arbre a) {
		return generer(a,"0");
	}
	
	public String generer(Arbre a, String pid) {
		String res = "";
		String id=pid;
		switch(a.getType()) {
		case SEMI:
			res=generer(a.getFg());
			res+="\n\tpop eax\n";
			res+=generer(a.getFd());
			break;
		case LET:
			res=generer(a.getFd());
			id = a.getFg().getValue();
			dataListe.add(id);
			res+="\n\tpop eax";
			res+="\n\tmov "+id+", eax";
			res+="\n\tpush eax";
			break;
		case PLUS:
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tadd eax, ebx";
			res+="\n\tpush eax";
			break;
		case MOINS:
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tsub eax, ebx";
			res+="\n\tpush eax";
			break;
		case MOINS_UNAIRE:
			res= generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tmov eax, 0";
			res+="\n\tsub eax, ebx";
			res+="\n\tpush eax";
			break;
		case DIV:
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tdiv eax, ebx";
			res+="\n\tpush eax";
			break;
		case MUL:
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tmul eax, ebx";
			res+="\n\tpush eax";
			break;
		case IDENT:
		case ENTIER:
			res="\tpush "+a.toString();
			break;
		case INPUT:
			res="\tin eax";
			res+="\n\tpush eax";
			break;
		case OUTPUT:
			res=generer(a.getFd());
			res+="\n\tpop eax";
			res+="\n\tout eax";
			break;
		case AND:
			idOpBin+=1;
			id=idOpBin.toString();
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tadd eax, ebx";
			res+="\n\tsub eax, 2";
			res+="\n\tjnz and_false_"+id;
			res+="\n\tpush 1";
			res+="\n\tjmp and_fin_"+id;
			res+="\nand_false_"+id+":";
			res+="\n\tpush 0";
			res+="\nand_fin_"+id+":";
			break;
		case DO:
			res=generer(a.getFd());
			break;
		case EGAL:
			idOpBin+=1;
			id=idOpBin.toString();
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tsub eax, ebx";
			res+="\n\tjnz egal_false_"+id;
			res+="\n\tpush 1";
			res+="\n\tjmp egal_fin_"+id;
			res+="\negal_false_"+id+":";
			res+="\n\tpush 0";
			res+="\negal_fin_"+id+":";
			break;
		case ELSE:
			res=generer(a.getFd());
			res+="\nsortie_if_"+id+":";
			break;
		case ERROR: //TODO
			break;
		case GT: 
			idGt +=1;
			id=idGt.toString();
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop eax";
			res+="\n\tpop ebx";
			res+="\n\tsub eax, ebx";
			res+="\n\tjle faux_gt_"+id;
			res+="\n\tmov eax,1";
			res+="\n\tjmp sortie_gt_"+id;
			res+="\nfaux_gt_"+id+":";
			res+="\n\tmov eax,0";
			res+="\nsortie_gt_"+id+":";
			res+="\n\tpush eax";
			break;
		case GTE:
			idGte +=1;
			id = idGte.toString();
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop eax";
			res+="\n\tpop ebx";
			res+="\n\tsub eax, ebx";
			res+="\n\tjle faux_gt_"+id;
			res+="\n\tmov eax,1";
			res+="\n\tjmp sortie_gt_"+id;
			res+="\nfaux_gt_"+id+":";
			res+="\n\tmov eax,0";
			res+="\nsortie_gt_"+id+":";
			res+="\n\tpush eax";
			break;
		case IF:
			idIf +=1;
			id = idIf.toString();
			res=generer(a.getFg());
			res+="\n\tjz faux_if_"+id;
			res+="\n"+generer(a.getFd(), id);
			break;
		case MOD:
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tmov ecx, eax";
			res+="\n\tdiv eax, ebx";
			res+="\n\tmul eax, ebx";
			res+="\n\tsub ecx, eax";
			res+="\n\tpush ecx";
			break;
		case NIL:
			res="nop";
			break;
		case NOT: 
			res=generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tmov eax, 1";
			res+="\n\tsub eax, ebx";
			res+="\n\tpush eax";
			break;
		case OR:
			idOpBin+=1;
			id=idOpBin.toString();
			res=generer(a.getFg());
			res+="\n"+generer(a.getFd());
			res+="\n\tpop ebx";
			res+="\n\tpop eax";
			res+="\n\tadd eax, ebx";
			res+="\n\tjz or_false_"+id;
			res+="\n\tpush 1";
			res+="\n\tjmp or_fin_"+id;
			res+="\nor_false_"+id+":";
			res+="\n\tpush 0";
			res+="\nor_fin_"+id+":";
			break;
		case THEN:
			res=generer(a.getFg());
			if(a.getFd()!=null && a.getFd().getType() == NodeType.ELSE)
				res+="\n\tjmp sortie_if_"+id;
			res+="\nfaux_if_"+id+":";
			res+="\n"+generer(a.getFd(),id);
			break;
		case WHILE:
			idWhile+=1;
			id=idWhile.toString();
			res="debut_while_"+id+":";
			res+="\n"+generer(a.getFg());
			res+="\n\tjz sortie_while_"+id;
			res+="\n"+generer(a.getFd());
			res+="\n\tjmp debut_while_"+id;
			res+="\nsortie_while_"+id+":";
			break;
		default:
			break;
		}
		
		return res;
	}
}
