import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './memory-picture.reducer';
import { IMemoryPicture } from 'app/shared/model/memory-picture.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemoryPictureDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemoryPictureDetail = (props: IMemoryPictureDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memoryPictureEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadmaanApp.memoryPicture.detail.title">MemoryPicture</Translate> [<b>{memoryPictureEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="image">
              <Translate contentKey="yaadmaanApp.memoryPicture.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {memoryPictureEntity.image ? (
              <div>
                {memoryPictureEntity.imageContentType ? (
                  <a onClick={openFile(memoryPictureEntity.imageContentType, memoryPictureEntity.image)}>
                    <img
                      src={`data:${memoryPictureEntity.imageContentType};base64,${memoryPictureEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {memoryPictureEntity.imageContentType}, {byteSize(memoryPictureEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="yaadmaanApp.memoryPicture.memory">Memory</Translate>
          </dt>
          <dd>{memoryPictureEntity.memoryId ? memoryPictureEntity.memoryId : ''}</dd>
        </dl>
        <Button tag={Link} to="/memory-picture" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memory-picture/${memoryPictureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ memoryPicture }: IRootState) => ({
  memoryPictureEntity: memoryPicture.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemoryPictureDetail);
