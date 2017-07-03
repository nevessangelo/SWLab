begin work;
drop table if exists dssearch."Dataset" cascade;
CREATE TABLE if not exists dssearch."Dataset" (
  dataset_id serial NOT NULL,
  dataset_uri text NOT NULL,
  feature_uri text NOT NULL,
  feature_type varchar(3) NOT NULL,
  frequency double precision NULL,
  CONSTRAINT "Dataset_pkey" PRIMARY KEY (dataset_id)
) WITH (OIDS=FALSE);
end work;

begin work;
ALTER TABLE dssearch."Dataset" OWNER TO lapaesleme;
drop index if exists dssearch."Dataset_idx1";
CREATE INDEX "Dataset_idx1" ON dssearch."Dataset" USING btree (dataset_uri ASC NULLS LAST);
end work; 