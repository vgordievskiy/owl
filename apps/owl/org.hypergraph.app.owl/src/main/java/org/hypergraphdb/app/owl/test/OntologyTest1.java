package org.hypergraphdb.app.owl.test;

import static org.junit.Assert.*;

import org.hypergraphdb.app.owl.HGDBOntologyRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * OntologyTest1.
 * @author Thomas Hilpold (CIAO/Miami-Dade County)
 * @created Oct 14, 2011
 */
public class OntologyTest1 {
	
	static boolean USE_HYPERGRAPH_ONTOLOGY = true;
	
	OWLOntology o;
	OWLOntologyManager m;
	OWLDataFactory df;
	// HGDBOntologyRepository r;
	IRI ontoIRI = IRI.create("hgdb://UNITTESTONTO1");

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		if (USE_HYPERGRAPH_ONTOLOGY) {
			m = HGDBOntologyRepository.createOWLOntologyManager();
		} else {
			m = OWLManager.createOWLOntologyManager();
		}
		df = m.getOWLDataFactory();
		IRI ontoIRI = IRI.create("hgdb://UNITTESTONTO 1");
		o = m.createOntology(ontoIRI);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.hypergraphdb.app.owl.HGDBOntologyImpl#isDeclared(org.semanticweb.owlapi.model.OWLEntity)}.
	 */
	@Test
	public void testClassCreationAndSubclass() {
		OWLClass cA = df.getOWLClass(IRI.create("A"));
		OWLClass cB = df.getOWLClass(IRI.create("B"));
		OWLClass cC = df.getOWLClass(IRI.create("C"));
		OWLClass cC2 = df.getOWLClass(IRI.create("C"));
		OWLDeclarationAxiom dA = df.getOWLDeclarationAxiom(cA);
		OWLDeclarationAxiom dB = df.getOWLDeclarationAxiom(cB);
		OWLDeclarationAxiom dC = df.getOWLDeclarationAxiom(cC);
		OWLDeclarationAxiom dC2 = df.getOWLDeclarationAxiom(cC);
		m.addAxiom(o, dA);
		m.addAxiom(o, dB);
		m.addAxiom(o, dC);
		OWLSubClassOfAxiom dBA = df.getOWLSubClassOfAxiom(cB, cA);
		OWLSubClassOfAxiom dCB = df.getOWLSubClassOfAxiom(cC, cB);
		OWLSubClassOfAxiom dCB2 = df.getOWLSubClassOfAxiom(cC, cB);
		m.addAxiom(o, dBA);
		m.addAxiom(o, dCB);
		//we have 2 classes A,B,C. and A<-B<-C 2 Subclass Axioms.
		assertTrue("", o.getAxiomCount() == 5);
		assertTrue("", o.getClassAssertionAxioms(cA).size() == 0);
		assertTrue("", o.getAxiomCount(AxiomType.SUBCLASS_OF) == 2);
		assertTrue("", o.getAxiomCount(AxiomType.DECLARATION) == 3);
		assertTrue("", o.getSignature().size() == 3);
		assertTrue("", o.getClassesInSignature().size() == 3);
		assertTrue("", o.getReferencingAxioms(cA).size() == 2);
		assertTrue("", o.getReferencingAxioms(cB).size() == 3);
		assertTrue("", o.containsAxiom(dA));
		assertTrue("", o.containsAxiom(dB));
		assertTrue("", o.containsAxiom(dC));
		assertTrue("", o.containsAxiom(dBA));
		assertTrue("", o.containsAxiom(dCB));
		assertTrue("", o.containsClassInSignature(cA.getIRI()));
		assertTrue("", o.containsEntityInSignature(cA));
		assertTrue("", o.getAxioms().contains(dBA));
		assertTrue("", o.getAxioms().contains(dCB));
		assertTrue("", o.getAxioms(cB).contains(dBA));
		//2011.10.14 THIS IS THE CULPRIT !!!
		//NExt line works with HGDB but breaks with Manchester - as it should be.
		// Was assertTrue, has to be assertFalse.
		//Implementing new sematics based on javadoc for getAxioms Method!
		assertFalse("", o.getAxioms(cB).contains(dCB));
		assertTrue("", o.getDeclarationAxioms(cB).size() == 1);
		assertTrue("", o.getEntitiesInSignature(cB.getIRI()).size() == 1);
		assertTrue("", o.getEntitiesInSignature(cB.getIRI()).contains(cB));
		assertTrue("", !o.getEntitiesInSignature(cB.getIRI()).contains(cA));
		assertTrue("", o.getReferencingAxioms(cB).size() == 3);
		assertTrue("", o.getReferencingAxioms(cC).size() == 2);
		assertTrue("", o.getSubClassAxiomsForSubClass(cA).size() == 0);
		assertTrue("", o.getSubClassAxiomsForSubClass(cB).size() == 1);
		assertTrue("", o.getSubClassAxiomsForSubClass(cC).size() == 1);
		assertTrue("", o.getSubClassAxiomsForSuperClass(cA).size() == 1);
		assertTrue("", o.getSubClassAxiomsForSuperClass(cB).size() == 1);
		assertTrue("", o.getSubClassAxiomsForSuperClass(cC).size() == 0);
		//TEST TEST AXIOMS 
		assertTrue("", o.containsAxiom(dCB2));
		assertTrue("", o.containsAxiom(dC2));
		assertFalse("", o.getSubClassAxiomsForSubClass(cA).contains(dCB2));
		assertFalse("", o.getSubClassAxiomsForSubClass(cB).contains(dCB2));
		assertTrue("", o.getSubClassAxiomsForSubClass(cC).contains(dCB2));
		assertFalse("", o.getSubClassAxiomsForSuperClass(cA).contains(dCB2));
		assertTrue("", o.getSubClassAxiomsForSuperClass(cB).contains(dCB2));
		assertFalse("", o.getSubClassAxiomsForSuperClass(cC).contains(dCB2));
		assertTrue("", dC.getSignature().size() == 1);		
		assertTrue("", dC.getSignature().contains(cC2));
		assertTrue("", dC.getSignature().contains(cC));
		assertTrue("", dC.getClassesInSignature().contains(cC2));
		assertTrue("", dC.getSignature().contains(cC));
		assertTrue("", dC.getClassesInSignature().contains(cC));
		assertTrue("", dC.getSignature().contains(cC));
		assertTrue("", o.containsAxiom(dCB2));
		assertTrue("", o.containsAxiom(dCB2));
		assertTrue("", o.containsAxiom(dCB2));
		assertTrue("", o.containsAxiom(dCB2));
		assertTrue("", o.containsAxiom(dCB2));
		
	}
		

}
