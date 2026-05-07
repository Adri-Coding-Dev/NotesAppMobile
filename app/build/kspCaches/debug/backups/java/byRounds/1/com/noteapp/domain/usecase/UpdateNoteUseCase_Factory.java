package com.noteapp.domain.usecase;

import com.noteapp.domain.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UpdateNoteUseCase_Factory implements Factory<UpdateNoteUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public UpdateNoteUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateNoteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateNoteUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new UpdateNoteUseCase_Factory(repositoryProvider);
  }

  public static UpdateNoteUseCase newInstance(NoteRepository repository) {
    return new UpdateNoteUseCase(repository);
  }
}
