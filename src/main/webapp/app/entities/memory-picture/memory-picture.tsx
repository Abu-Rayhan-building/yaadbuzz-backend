import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './memory-picture.reducer';
import { IMemoryPicture } from 'app/shared/model/memory-picture.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemoryPictureProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const MemoryPicture = (props: IMemoryPictureProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { memoryPictureList, match, loading } = props;
  return (
    <div>
      <h2 id="memory-picture-heading">
        <Translate contentKey="yaadmaanApp.memoryPicture.home.title">Memory Pictures</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.memoryPicture.home.createLabel">Create new Memory Picture</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {memoryPictureList && memoryPictureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.memoryPicture.image">Image</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.memoryPicture.memory">Memory</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {memoryPictureList.map((memoryPicture, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${memoryPicture.id}`} color="link" size="sm">
                      {memoryPicture.id}
                    </Button>
                  </td>
                  <td>
                    {memoryPicture.image ? (
                      <div>
                        {memoryPicture.imageContentType ? (
                          <a onClick={openFile(memoryPicture.imageContentType, memoryPicture.image)}>
                            <img
                              src={`data:${memoryPicture.imageContentType};base64,${memoryPicture.image}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {memoryPicture.imageContentType}, {byteSize(memoryPicture.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{memoryPicture.memoryId ? <Link to={`memory/${memoryPicture.memoryId}`}>{memoryPicture.memoryId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${memoryPicture.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${memoryPicture.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${memoryPicture.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="yaadmaanApp.memoryPicture.home.notFound">No Memory Pictures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ memoryPicture }: IRootState) => ({
  memoryPictureList: memoryPicture.entities,
  loading: memoryPicture.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemoryPicture);
