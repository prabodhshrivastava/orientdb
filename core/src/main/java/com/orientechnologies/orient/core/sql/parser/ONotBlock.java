/* Generated By:JJTree: Do not edit this line. ONotBlock.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.metadata.schema.OClass;

import java.util.List;
import java.util.Map;

public class ONotBlock extends OBooleanExpression {
  protected OBooleanExpression sub;

  protected boolean            negate = false;

  public ONotBlock(int id) {
    super(id);
  }

  public ONotBlock(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public boolean evaluate(OIdentifiable currentRecord, OCommandContext ctx) {
    if (sub == null) {
      return true;
    }
    boolean result = sub.evaluate(currentRecord, ctx);
    if (negate) {
      return !result;
    }
    return result;
  }

  public OBooleanExpression getSub() {
    return sub;
  }

  public void setSub(OBooleanExpression sub) {
    this.sub = sub;
  }

  public boolean isNegate() {
    return negate;
  }

  public void setNegate(boolean negate) {
    this.negate = negate;
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    if (negate) {
      builder.append("NOT ");
    }
    sub.toString(params, builder);
  }

  @Override
  public boolean supportsBasicCalculation() {
    return true;
  }

  @Override
  protected int getNumberOfExternalCalculations() {
    return sub.getNumberOfExternalCalculations();
  }

  @Override
  protected List<Object> getExternalCalculationConditions() {
    return sub.getExternalCalculationConditions();
  }

  public List<OBinaryCondition> getIndexedFunctionConditions(OClass iSchemaClass, ODatabaseDocumentInternal database) {
    if (sub == null) {
      return null;
    }
    if (negate) {
      return null;
    }
    return sub.getIndexedFunctionConditions(iSchemaClass, database);
  }

  @Override public List<OAndBlock> flatten() {
    if(!negate){
      return sub.flatten();
    }
    return super.flatten();
  }

  @Override public List<String> getMatchPatternInvolvedAliases() {
    return sub.getMatchPatternInvolvedAliases();
  }
}
/* JavaCC - OriginalChecksum=1926313b3f854235aaa20811c22d583b (do not edit this line) */
