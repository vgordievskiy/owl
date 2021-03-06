package org.hypergraphdb.app.owl.versioning.distributed.serialize;

import java.io.Writer;



import org.hypergraphdb.app.owl.HGDBOntology;
import org.hypergraphdb.app.owl.HGDBOntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AbstractOWLOntologyStorer;

/**
 * VOWLXMLOntologyStorer will store a versioned ontology with all revisions,
 * committed changesets and head ontology data. The ontology has to be database
 * backed and has to be version controlled.
 * 
 * @see VOWLXMLRenderConfiguration
 * 
 * @author Thomas Hilpold (CIAO/Miami-Dade County)
 * @created Mar 14, 2012
 */
public class VOWLXMLOntologyStorer extends AbstractOWLOntologyStorer
{

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.semanticweb.owlapi.model.OWLOntologyStorer#canStoreOntology(org.
	 * semanticweb.owlapi.model.OWLOntologyFormat)
	 */
	@Override
	public boolean canStoreOntology(OWLOntologyFormat ontologyFormat)
	{
		return ontologyFormat.getClass().equals(VOWLXMLOntologyFormat.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.AbstractOWLOntologyStorer#storeOntology(org
	 * .semanticweb.owlapi.model.OWLOntologyManager,
	 * org.semanticweb.owlapi.model.OWLOntology, java.io.Writer,
	 * org.semanticweb.owlapi.model.OWLOntologyFormat)
	 */
	@Override
	protected void storeOntology(OWLOntologyManager manager, OWLOntology ontology, Writer writer, OWLOntologyFormat format)
			throws OWLOntologyStorageException
	{
		if (ontology instanceof HGDBOntology)
		{
			VOWLXMLVersionedOntologyRenderer renderer = new VOWLXMLVersionedOntologyRenderer((HGDBOntologyManager)manager);
			renderer.render(ontology, writer);
		}
		else
		{
			throw new OWLOntologyStorageException("Can only store database backed versioned ontologies.");
		}
	}

	@Override
	protected void storeOntology(OWLOntology ontology, Writer writer, OWLOntologyFormat format) throws OWLOntologyStorageException
	{
		storeOntology(ontology.getOWLOntologyManager(), ontology, writer, format);
	}
}