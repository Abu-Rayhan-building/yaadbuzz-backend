import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './topic.reducer';
import { ITopic } from 'app/shared/model/topic.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITopicProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Topic = (props: ITopicProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { topicList, match, loading } = props;
  return (
    <div>
      <h2 id="topic-heading">
        <Translate contentKey="yaadmaanApp.topic.home.title">Topics</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.topic.home.createLabel">Create new Topic</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {topicList && topicList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topic.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topic.department">Department</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topic.voters">Voters</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {topicList.map((topic, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${topic.id}`} color="link" size="sm">
                      {topic.id}
                    </Button>
                  </td>
                  <td>{topic.title}</td>
                  <td>{topic.departmentId ? <Link to={`department/${topic.departmentId}`}>{topic.departmentId}</Link> : ''}</td>
                  <td>
                    {topic.voters
                      ? topic.voters.map((val, j) => (
                          <span key={j}>
                            <Link to={`user-per-department/${val.id}`}>{val.id}</Link>
                            {j === topic.voters.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${topic.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${topic.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${topic.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="yaadmaanApp.topic.home.notFound">No Topics found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ topic }: IRootState) => ({
  topicList: topic.entities,
  loading: topic.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Topic);
