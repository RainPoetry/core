package rainpoetry.sql.spark.core

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */


abstract class ExecutePlan extends ExecuteNode[ExecutePlan] {

}

abstract class SqlPlan extends ExecutePlan {

}


abstract class SparkCodePlan extends ExecutePlan {

}