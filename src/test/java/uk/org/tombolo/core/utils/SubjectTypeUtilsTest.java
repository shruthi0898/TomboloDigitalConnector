package uk.org.tombolo.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.org.tombolo.AbstractTest;
import uk.org.tombolo.core.SubjectType;

public class SubjectTypeUtilsTest extends AbstractTest {
	
	@Test
	public void testGetSubjectTypeByLabel(){
		SubjectType lsoa = SubjectTypeUtils.getSubjectTypeByLabel("lsoa");
		assertEquals("lsoa", lsoa.getLabel());
		assertEquals("Lower Layer Super Output Area", lsoa.getName());
	}
}